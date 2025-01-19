import React, { useState } from 'react';
import Keycloak from 'keycloak-js';
import { httpClient } from './httpClient';
import "./App.css";
import Footer from './Footer';
import MainBlock from './MainBlock';
import Topbar from './TopBar';

let initOprions = {
  url: 'http://localhost:8181/',
  realm: 'projekt-jps',
  clientId: 'frontend-user'
};

let kc = new Keycloak(initOprions);

kc.init({
  onLoad: 'login-required',
  silentCheckSsoRedirectUri: window.location.origin + "/silent-check-sso.html",
  checkLoginIframe: true,
  pkceMethod: 'S256'
}).then((auth) => {
  if (!auth) {
    window.location.reload();
  } else {
    console.info("Authenticated");
    console.log('auth', auth);
    console.log('Keycloak', kc);
    console.log('Access Token', kc.token);
    httpClient.defaults.headers.common['Authorization'] = `Bearer ${kc.token}`;
    kc.onTokenExpired = () => {
      console.log('token expired');
    };
  }
}, () => {
  console.error("Authentication Failed");
});

function App() {
  const [value, setValue] = useState(null);
  const changeValue = (newValue) => {
    setValue(newValue);
  };
  return (
    <div className='window'>
      <Topbar kc={kc} />
      <MainBlock x={value} y={changeValue} kc={kc}/>
      <Footer />
    </div>
  );
}

export default App;