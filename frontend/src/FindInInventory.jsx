import React, { useState } from 'react';
import axios from 'axios';
import './App.css';

function FindInInventory({ kc }) {
  const [productName, setProductName] = useState('');
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [inventoryData, setInventoryData] = useState(null);

  const handleChange = (e) => {
    setProductName(e.target.value);
  };

  const validateForm = () => {
    if (!productName.trim()) {
      setError('Product name is required');
      return false;
    }
    setError('');
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    try {
      const response = await axios.get(`http://localhost:9000/api/inventory/find`, {
        params: { name: productName },
        headers: {
          Authorization: `Bearer ${kc.token}`
        }
      });

      if (response.status === 200 && response.data) {
        setInventoryData(response.data);
        setMessage(`Product found: ${response.data.name}`);
      } else {
        setMessage('Product not found.');
        setInventoryData(null);
      }
    } catch (error) {
      setMessage('Error: Unable to fetch product from inventory');
    }
  };

  return (
    <div className="find-in-inventory">
      <h3>Find Product in Inventory</h3>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Product Name:</label>
          <input
            type="text"
            name="productName"
            value={productName}
            onChange={handleChange}
            placeholder="Enter product name"
          />
          {error && <p className="error">{error}</p>}
        </div>

        <button type="submit">Find Product</button>
      </form>

      {message && <p className={message.includes('Error') ? 'error' : 'success'}>{message}</p>}

      {inventoryData && (
        <div className="inventory-result">
          <h4>Product Details</h4>
          <p>Name: {inventoryData.name}</p>
          <p>Quantity: {inventoryData.quantity}</p>
          <p>Description: {inventoryData.description}</p>
        </div>
      )}
    </div>
  );
}

export default FindInInventory;
