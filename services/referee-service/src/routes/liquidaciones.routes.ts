import { Router } from 'express';
import { authHeaders, coreClient } from '../http/clients.js';
import { handleProxyError } from '../utils/http.js';

const router = Router();

router.get('/', async (req, res) => {
  const token = req.headers.authorization;
  const arbitroId = req.query.arbitroId;

  if (typeof arbitroId !== 'string' || arbitroId.length === 0) {
    return res.status(400).json({ message: 'arbitroId es requerido' });
  }

  try {
    const response = await coreClient.get(`/api/liquidaciones/arbitro/${arbitroId}`, {
      headers: authHeaders(token)
    });
    return res.json(response.data);
  } catch (error) {
    return handleProxyError(error, res);
  }
});

router.get('/:id/reporte', async (req, res) => {
  const token = req.headers.authorization;
  const { id } = req.params;
  const formato = typeof req.query.formato === 'string' ? req.query.formato : 'pdf';

  try {
    const response = await coreClient.get(`/api/liquidaciones/${id}/reporte`, {
      headers: authHeaders(token),
      responseType: 'arraybuffer',
      params: { formato }
    });

    const contentType = response.headers['content-type'] ?? 'application/octet-stream';
    const disposition = response.headers['content-disposition'] ?? `attachment; filename=liquidacion-${id}.${formato}`;

    res.setHeader('Content-Type', contentType);
    res.setHeader('Content-Disposition', disposition);
    return res.send(Buffer.from(response.data));
  } catch (error) {
    return handleProxyError(error, res);
  }
});

export default router;

