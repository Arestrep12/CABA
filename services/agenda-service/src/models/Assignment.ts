import { Schema, model } from 'mongoose';

export interface AssignmentDocument {
  assignmentId: string;
  refereeId: string;
  matchId: string;
  scheduledAt: Date;
  role: string;
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED' | 'CANCELLED';
  createdAt: Date;
  updatedAt: Date;
}

const assignmentSchema = new Schema<AssignmentDocument>(
  {
    assignmentId: { type: String, required: true, unique: true },
    refereeId: { type: String, required: true, index: true },
    matchId: { type: String, required: true },
    scheduledAt: { type: Date, required: true },
    role: { type: String, required: true },
    status: {
      type: String,
      required: true,
      uppercase: true,
      enum: ['PENDING', 'ACCEPTED', 'REJECTED', 'CANCELLED'],
      default: 'PENDING',
    },
  },
  { timestamps: true },
);

assignmentSchema.index({ refereeId: 1, scheduledAt: 1 });

export const AssignmentModel = model<AssignmentDocument>('Assignment', assignmentSchema);

