import axios from 'axios';
import { useAuth } from './AuthContext';

const useAxios = () => {
  const { keycloak } = useAuth();

  const instance = axios.create();

  instance.interceptors.request.use(
    async (config) => {
      if (keycloak) {
        try {
          await keycloak.updateToken(30); 
          config.headers.Authorization = `Bearer ${keycloak.token}`;
        } catch (error) {
          console.error('Token renewal failed:', error);
        }
      }
      return config;
    },
    (error) => Promise.reject(error)
  );

  return instance;
};

export default useAxios;