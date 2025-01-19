import axios from 'axios';


 const httpClient = axios.create({
  baseURL: 'http://localhost:9000', 
  timeout: 5000, 
});




export { httpClient };