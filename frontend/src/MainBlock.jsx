import React from 'react';
import "./App.css";
import MenuBar from './MenuBar';
import ContentDefault from './Content';

function MainBlock({ x, y, kc}) {
  return (
    <div className='MainBlock'>
      <MenuBar changeValue={y} />
      <ContentDefault x={x} kc={kc} />
    </div>
  );
}

export default MainBlock;