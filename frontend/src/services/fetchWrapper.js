import authService from './authService';

export async function fetchComToken(url, options = {}) {
    const token = authService.getToken();

    const headers = {
        ...options.headers,
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    };

    const response = await fetch(url, {
        ...options,
        headers
    });

    if (response.status === 401 || response.status === 403) {
        authService.logout();
        window.location.href = '/login';
    }

    return response.json();
}
