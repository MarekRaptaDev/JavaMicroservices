import React from 'react';
import "./App.css";

function MenuBar({ changeValue }) {
  return (
    <div className='Menu'>
      <button id='SeeAllProducts' onClick={() => changeValue(0)}>See All Products</button>
      <button id='PlaceOrder' onClick={() => changeValue(1)}>Place Order</button>
      <button id='AddNewProduct' onClick={() => changeValue(2)}>Add New Product</button>
      <button id='FindInInventory' onClick={() => changeValue(3)}>Find in Inventory</button>
      <button id='UpdateInventory' onClick={() => changeValue(4)}>Update Inventory Item Quantity</button>
      
    </div>
  );
}

export default MenuBar;
