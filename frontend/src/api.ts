import axios from 'axios';

export const api = axios.create({
  baseURL: '/api'
});

export type ApiResponse<T> = { success: boolean; message?: string; data: T };


