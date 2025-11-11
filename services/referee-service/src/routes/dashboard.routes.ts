import { Router } from 'express';
import { authHeaders, coreClient } from '../http/clients.js';
import { handleProxyError } from '../utils/http.js';

const router = Router();

router.get('/', async (req, res) => {
  const token = req.headers.authorization;
  try {
    const response = await coreClient.get('/api/dashboard/resumen', {
      headers: authHeaders(token)
    });
    return res.json(response.data);
  } catch (error) {
    return handleProxyError(error, res);
  }
});

export default router;

