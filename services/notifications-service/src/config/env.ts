import { z } from 'zod';

const schema = z.object({
  NODE_ENV: z.enum(['development', 'test', 'production']).default('development'),
  PORT: z.coerce.number().int().min(0).default(4002),
  REDIS_URL: z.string().min(1, 'REDIS_URL es obligatorio'),
  MAIL_FROM: z.string().email('MAIL_FROM inválido'),
  MAIL_TRANSPORT: z.string().min(1, 'MAIL_TRANSPORT es obligatorio'),
});

export type Env = z.infer<typeof schema>;

let cache: Env | null = null;

export function loadEnv(source: NodeJS.ProcessEnv = process.env): Env {
  if (cache) return cache;
  const parsed = schema.safeParse(source);
  if (!parsed.success) {
    throw new Error(
      `Variables de entorno notifications-service incorrectas: ${parsed.error.message}`,
    );
  }
  cache = parsed.data;
  return cache;
}

export function resetEnv() {
  cache = null;
}

