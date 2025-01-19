import React from 'react';
import './App.css';
import SeeAllProducts from './SeeAll';
import PlaceOrder from './PlaceOrder';
import AddNewProduct from './AddNewProduct';
import FindInInventory from './FindInInventory';
import UpdateInventoryQuantity from './UpdateQuantity';


function ContentDefault({ x,  kc}) {
  switch (x) {
    case 0:
      return <SeeAllProducts kc={kc} />;
    case 1:
      return <PlaceOrder kc={kc}/>;
    case 2:
      return <AddNewProduct kc={kc}/>;
    case 3:
      return <FindInInventory kc={kc}/>;
    case 4:
      return <UpdateInventoryQuantity kc={kc}/>;
    default:
      return (
        <div className='Content'>
          <span id='Welcome'>Welcome, what would you want to do?</span>
        </div>
      );
  }
}

export default ContentDefault;