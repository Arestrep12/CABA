import { Router } from 'express';
import { agendaClient } from '../http/clients.js';
import { handleProxyError } from '../utils/http.js';

const router = Router();

router.get('/:refereeId', async (req, res) => {
  const { refereeId } = req.params;
  try {
    const response = await agendaClient.get(`/availability/${refereeId}`);
    return res.json(response.data);
  } catch (error) {
    return handleProxyError(error, res);
  }
});

router.post('/', async (req, res) => {
  try {
    await agendaClient.post('/availability', req.body);
    return res.status(204).send();
  } catch (error) {
    return handleProxyError(error, res);
  }
});

export default router;

