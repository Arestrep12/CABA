import { Router } from 'express';
import { z } from 'zod';
import { AvailabilityModel } from '../models/Availability.js';
import { logger } from '../config/logger.js';

const router = Router();

const availabilitySchema = z.object({
  refereeId: z.string().uuid(),
  slots: z
    .array(
      z.object({
        dayOfWeek: z.number().int().min(0).max(6),
        startTime: z.string().regex(/^\d{2}:\d{2}$/),
        endTime: z.string().regex(/^\d{2}:\d{2}$/),
      }),
    )
    .nonempty(),
});

router.post('/', async (req, res, next) => {
  try {
    const { refereeId, slots } = availabilitySchema.parse(req.body);
    await AvailabilityModel.deleteMany({ refereeId });
    await AvailabilityModel.insertMany(
      slots.map((slot) => ({
        refereeId,
        dayOfWeek: slot.dayOfWeek,
        startTime: slot.startTime,
        endTime: slot.endTime,
      })),
    );
    logger.info({ refereeId, slots: slots.length }, 'Disponibilidad registradas');
    return res.status(204).send();
  } catch (error) {
    return next(error);
  }
});

router.get('/:refereeId', async (req, res, next) => {
  try {
    const refereeId = z.string().uuid().parse(req.params.refereeId);
    const slots = await AvailabilityModel.find({ refereeId }).lean();
    return res.json({ refereeId, slots });
  } catch (error) {
    return next(error);
  }
});

export default router;

