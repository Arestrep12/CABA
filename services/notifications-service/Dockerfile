FROM node:20-slim AS build
WORKDIR /workspace

ENV PNPM_HOME=/root/.local/share/pnpm
ENV PATH="${PNPM_HOME}:${PATH}"
RUN corepack enable

COPY pnpm-lock.yaml pnpm-workspace.yaml package.json ./
COPY services/notifications-service/package.json services/notifications-service/
RUN pnpm install --filter @caba/notifications-service...

COPY tsconfig.base.json ./
COPY services/notifications-service services/notifications-service
RUN pnpm --filter @caba/notifications-service build
RUN pnpm --filter @caba/notifications-service deploy --prod /opt/notifications-service

FROM node:20-slim AS runner
WORKDIR /app
ENV NODE_ENV=production
ENV PNPM_HOME=/root/.local/share/pnpm
ENV PATH="${PNPM_HOME}:${PATH}"
RUN corepack enable

COPY --from=build /opt/notifications-service .

EXPOSE 4002
CMD ["node","dist/index.js"]

