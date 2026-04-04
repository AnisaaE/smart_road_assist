import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
  stages: [
    { duration: '30s', target: 50 },
    { duration: '1m', target: 100 },
    { duration: '30s', target: 500 },
  ],
  thresholds: {
    http_req_failed: ['rate<0.1'],
    http_req_duration: ['p(95)<2000'],
  },
};

export function setup() {
  const uniqueSuffix = Date.now();
  const credentials = {
    name: 'k6 Load User',
    email: `k6-user-${uniqueSuffix}@smartassist.com`,
    phone: `555${uniqueSuffix}`.slice(0, 12),
    password: 'k6-password-123',
    role: 'USER',
  };

  const registerResponse = registerLoadTestUser(credentials);

  check(registerResponse, {
    'register status is 200 or 201': (res) => res.status === 200 || res.status === 201,
  });

  const registerBody = safeJson(registerResponse);
  if (!registerBody || !registerBody.token || !registerBody.userId) {
    throw new Error(`Failed to create load-test user. Status: ${registerResponse.status}, Body: ${registerResponse.body}`);
  }

  return {
    token: registerBody.token,
    userId: registerBody.userId,
  };
}

export default function (data) {
  const authHeaders = {
    Authorization: `Bearer ${data.token}`,
    'Content-Type': 'application/json',
  };

  const requestPayload = {
    userId: data.userId,
    type: 'BATTERY',
    description: `k6 generated request ${__VU}-${__ITER}`,
    location: `Istanbul-${__VU}`,
  };

  const responses = http.batch([
    {
      method: 'GET',
      url: `${BASE_URL}/api/requests`,
      params: {
        headers: authHeaders,
        tags: { endpoint: 'get-requests' },
      },
    },
    {
      method: 'POST',
      url: `${BASE_URL}/api/requests`,
      body: JSON.stringify(requestPayload),
      params: {
        headers: authHeaders,
        tags: { endpoint: 'create-request' },
      },
    },
  ]);

  check(responses[0], {
    'GET /api/requests status is 200 or 201': (res) => res.status === 200 || res.status === 201,
  });

  check(responses[1], {
    'POST /api/requests status is 200 or 201': (res) => res.status === 200 || res.status === 201,
    'POST /api/requests returns request id': (res) => {
      const body = safeJson(res);
      return !!(body && body.id);
    },
  });

  sleep(1);
}

function safeJson(response) {
  try {
    return response.json();
  } catch (error) {
    return null;
  }
}

function registerLoadTestUser(credentials) {
  let lastResponse = null;

  for (let attempt = 1; attempt <= 5; attempt += 1) {
    lastResponse = http.post(
      `${BASE_URL}/register`,
      JSON.stringify(credentials),
      {
        headers: { 'Content-Type': 'application/json' },
        tags: { endpoint: 'register' },
        timeout: '30s',
      }
    );

    if (lastResponse && lastResponse.status && lastResponse.status >= 200 && lastResponse.status < 300) {
      return lastResponse;
    }

    sleep(Math.min(attempt, 3));
  }

  return lastResponse;
}
