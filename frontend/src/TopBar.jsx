import React from 'react';
import "./App.css";

function Topbar({ kc }) {
  const handleLogout = () => {
    kc.logout();
  };

  return (
    <div className='Topbar'>
      <div className='UserInfo'>
        <span>Test</span>
      </div>
      <button id='Logout' onClick={handleLogout}>Logout</button>
    </div>
  );
}

export default Topbar;
