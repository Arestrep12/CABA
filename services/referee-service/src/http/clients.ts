import axios from 'axios';
import { loadEnv } from '../config/env.js';

const env = loadEnv();

export const coreClient = axios.create({
  baseURL: env.CORE_API_URL,
  timeout: env.REQUEST_TIMEOUT_MS
});

export const agendaClient = axios.create({
  baseURL: env.AGENDA_API_URL,
  timeout: env.REQUEST_TIMEOUT_MS
});

export function authHeaders(token: string | undefined) {
  if (!token) return {};
  return { Authorization: token };
}

