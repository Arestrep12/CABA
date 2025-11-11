import { Router } from 'express';
import { z } from 'zod';
import { AssignmentModel } from '../models/Assignment.js';

const router = Router();

const upsertSchema = z.object({
  assignmentId: z.string().uuid(),
  refereeId: z.string().uuid(),
  matchId: z.string().uuid(),
  scheduledAt: z.string().datetime(),
  role: z.string().min(1),
  status: z.enum(['PENDING', 'ACCEPTED', 'REJECTED', 'CANCELLED']).default('PENDING'),
});

router.post('/', async (req, res, next) => {
  try {
    const payload = upsertSchema.parse(req.body);
    await AssignmentModel.findOneAndUpdate(
      { assignmentId: payload.assignmentId },
      {
        refereeId: payload.refereeId,
        matchId: payload.matchId,
        scheduledAt: new Date(payload.scheduledAt),
        role: payload.role,
        status: payload.status,
      },
      { upsert: true, new: true, setDefaultsOnInsert: true },
    );
    return res.status(204).send();
  } catch (error) {
    next(error);
  }
});

router.get('/referee/:refereeId', async (req, res, next) => {
  try {
    const refereeId = z.string().uuid().parse(req.params.refereeId);
    const assignments = await AssignmentModel.find({ refereeId })
      .sort({ scheduledAt: 1 })
      .lean();
    return res.json(assignments);
  } catch (error) {
    next(error);
  }
});

router.get('/:assignmentId', async (req, res, next) => {
  try {
    const assignmentId = z.string().uuid().parse(req.params.assignmentId);
    const assignment = await AssignmentModel.findOne({ assignmentId }).lean();
    if (!assignment) {
      return res.status(404).json({ message: 'Asignación no encontrada' });
    }
    return res.json(assignment);
  } catch (error) {
    next(error);
  }
});

export default router;

