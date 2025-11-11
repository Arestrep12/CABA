import { Queue, Worker, Job } from 'bullmq';
import { createTransport } from 'nodemailer';
import Redis from 'ioredis';
import { loadEnv } from './env.js';
import { logger } from './logger.js';

const env = loadEnv();

const connection =
  env.NODE_ENV === 'test'
    ? new (await import('ioredis-mock')).default(env.REDIS_URL)
    : new Redis(env.REDIS_URL);

export const notificationQueue = new Queue('notifications', {
  connection,
});

type NotificationJob = {
  to: string;
  subject: string;
  template: string;
  variables?: Record<string, unknown>;
};

const transport = createTransport(env.MAIL_TRANSPORT);

async function processJob(job: Job<NotificationJob>) {
  logger.info({ jobId: job.id, to: job.data.to }, 'Procesando notificación');
  const renderedTemplate = renderTemplate(job.data.template, job.data.variables);
  await transport.sendMail({
    from: env.MAIL_FROM,
    to: job.data.to,
    subject: job.data.subject,
    html: renderedTemplate,
  });
  logger.info({ jobId: job.id }, 'Notificación enviada');
}

export let notificationWorker: Worker<NotificationJob> | null = null;

if (env.NODE_ENV !== 'test') {
  notificationWorker = new Worker<NotificationJob>(
    'notifications',
    async (job) => processJob(job),
    { connection },
  );

  notificationWorker.on('failed', (job, err) => {
    logger.error({ jobId: job?.id, err }, 'Notificación fallida');
  });
}

function renderTemplate(template: string, variables: Record<string, unknown> = {}) {
  return template.replace(/\{\{(\w+)\}\}/g, (_match, key) => {
    const value = variables[key];
    return value == null ? '' : String(value);
  });
}

