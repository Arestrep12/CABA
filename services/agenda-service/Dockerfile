FROM node:20-slim AS build
WORKDIR /workspace

ENV PNPM_HOME=/root/.local/share/pnpm
ENV PATH="${PNPM_HOME}:${PATH}"
RUN corepack enable

COPY pnpm-lock.yaml pnpm-workspace.yaml package.json ./
COPY services/agenda-service/package.json services/agenda-service/
RUN pnpm install --filter @caba/agenda-service...

COPY tsconfig.base.json ./
COPY services/agenda-service services/agenda-service
RUN pnpm --filter @caba/agenda-service build
RUN pnpm --filter @caba/agenda-service deploy --prod /opt/agenda-service

FROM node:20-slim AS runner
WORKDIR /app
ENV NODE_ENV=production
ENV PNPM_HOME=/root/.local/share/pnpm
ENV PATH="${PNPM_HOME}:${PATH}"
RUN corepack enable

COPY --from=build /opt/agenda-service .

EXPOSE 4001
CMD ["node","dist/index.js"]

