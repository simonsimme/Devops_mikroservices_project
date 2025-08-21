# Gateway Service

This directory will contain the API Gateway service that routes requests to appropriate microservices.

## Structure
When implemented, this will contain:
- `package.json` - Dependencies and scripts
- `Dockerfile` - Container definition
- `src/` - Source code
- `tests/` - Unit and integration tests
- `README.md` - Service-specific documentation

## Development
```bash
cd gateway
npm install
npm run dev  # Start development server
npm test     # Run tests
```