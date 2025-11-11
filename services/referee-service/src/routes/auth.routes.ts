import { Router } from 'express';
import { coreClient } from '../http/clients.js';
import { handleProxyError } from '../utils/http.js';

const router = Router();

router.post('/login', async (req, res) => {
  try {
    const response = await coreClient.post('/api/auth/login', req.body);
    return res.status(response.status).json(response.data);
  } catch (error) {
    return handleProxyError(error, res);
  }
});

export default router;

