FROM node:20-slim AS build
WORKDIR /workspace

ENV PNPM_HOME=/root/.local/share/pnpm
ENV PATH="${PNPM_HOME}:${PATH}"
RUN corepack enable

COPY pnpm-lock.yaml pnpm-workspace.yaml package.json ./
COPY services/referee-service/package.json services/referee-service/
RUN pnpm install --filter @caba/referee-service...

COPY tsconfig.base.json ./
COPY services/referee-service services/referee-service
RUN pnpm --filter @caba/referee-service build
RUN pnpm --filter @caba/referee-service deploy --prod /opt/referee-service

FROM node:20-slim AS runner
WORKDIR /app
ENV NODE_ENV=production
ENV PNPM_HOME=/root/.local/share/pnpm
ENV PATH="${PNPM_HOME}:${PATH}"
RUN corepack enable

COPY --from=build /opt/referee-service .

EXPOSE 4003
CMD ["node","dist/index.js"]

