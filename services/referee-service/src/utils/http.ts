import axios, { AxiosError } from 'axios';
import { Response } from 'express';
import { logger } from '../config/logger.js';

export function handleProxyError(error: unknown, res: Response) {
  if (axios.isAxiosError(error)) {
    const axiosError = error as AxiosError;
    const status = axiosError.response?.status ?? 502;
    const payload = axiosError.response?.data;
    logger.warn(
      {
        status,
        message: axiosError.message,
        url: axiosError.config?.url
      },
      'Error consumiendo API remota'
    );
    return res.status(status).json(
      typeof payload === 'object' && payload !== null
        ? payload
        : { message: 'Error al comunicarse con servicio remoto' }
    );
  }

  logger.error({ error }, 'Error inesperado en proxy');
  return res.status(500).json({ message: 'Error inesperado en referee-service' });
}

