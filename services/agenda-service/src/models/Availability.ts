import { Schema, model } from 'mongoose';

export interface AvailabilityDocument {
  refereeId: string;
  dayOfWeek: number; // 0-6
  startTime: string; // HH:mm
  endTime: string; // HH:mm
  createdAt: Date;
  updatedAt: Date;
}

const availabilitySchema = new Schema<AvailabilityDocument>(
  {
    refereeId: { type: String, required: true, index: true },
    dayOfWeek: { type: Number, required: true, min: 0, max: 6 },
    startTime: { type: String, required: true },
    endTime: { type: String, required: true },
  },
  { timestamps: true },
);

availabilitySchema.index(
  { refereeId: 1, dayOfWeek: 1, startTime: 1, endTime: 1 },
  { unique: true },
);

export const AvailabilityModel = model<AvailabilityDocument>(
  'Availability',
  availabilitySchema,
);

