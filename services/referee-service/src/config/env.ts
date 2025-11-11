import { z } from 'zod';

const envSchema = z.object({
  NODE_ENV: z.enum(['development', 'test', 'production']).default('development'),
  PORT: z.coerce.number().int().min(0).default(4003),
  CORE_API_URL: z.string().url('CORE_API_URL debe ser una URL válida'),
  AGENDA_API_URL: z.string().url('AGENDA_API_URL debe ser una URL válida'),
  REQUEST_TIMEOUT_MS: z.coerce.number().int().default(5000)
});

export type Env = z.infer<typeof envSchema>;

let cachedEnv: Env | null = null;

export function loadEnv(source: NodeJS.ProcessEnv = process.env): Env {
  if (cachedEnv) return cachedEnv;
  const parsed = envSchema.safeParse(source);
  if (!parsed.success) {
    throw new Error(
      `Variables de entorno inválidas para referee-service: ${parsed.error.flatten().fieldErrors}`
    );
  }
  cachedEnv = parsed.data;
  return cachedEnv;
}

export function resetEnvCache() {
  cachedEnv = null;
}

