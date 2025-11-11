import { z } from 'zod';

const envSchema = z.object({
  NODE_ENV: z.enum(['development', 'test', 'production']).default('development'),
  PORT: z.coerce.number().int().min(0).default(4001),
  MONGO_URL: z.string().min(1, 'MONGO_URL es obligatorio'),
  REDIS_URL: z.string().optional(),
});

export type Env = z.infer<typeof envSchema>;

let cached: Env | null = null;

export function loadEnv(source: NodeJS.ProcessEnv = process.env): Env {
  if (cached) return cached;
  const parsed = envSchema.safeParse(source);
  if (!parsed.success) {
    throw new Error(
      `Error en variables de entorno agenda-service: ${parsed.error.flatten().fieldErrors}`,
    );
  }
  cached = parsed.data;
  return cached;
}

export function resetEnvCache() {
  cached = null;
}
