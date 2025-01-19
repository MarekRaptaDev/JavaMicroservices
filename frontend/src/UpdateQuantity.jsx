import React, { useState } from 'react';
import axios from 'axios';
import './App.css';

function UpdateInventoryQuantity({ kc }) {
  const [productName, setProductName] = useState('');
  const [quantity, setQuantity] = useState('');
  const [message, setMessage] = useState('');
  const [messageClass, setMessageClass] = useState('');

  const handleProductNameChange = (e) => {
    setProductName(e.target.value);
  };

  const handleQuantityChange = (e) => {
    setQuantity(e.target.value);
  };

  const validateForm = () => {
    if (!productName.trim()) {
      setMessage('Product name is required.');
      setMessageClass('error');
      return false;
    }

    if (!quantity || isNaN(quantity) || parseInt(quantity) === 0) {
      setMessage('Quantity must be a non-zero integer.');
      setMessageClass('error');
      return false;
    }

    setMessage('');
    setMessageClass('');
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) return;

    try {
      const response = await axios.put(
        `http://localhost:9000/api/inventory/update`,
        null,
        {
          params: {
            name: productName,
            quantity: quantity
          },
          headers: {
            Authorization: `Bearer ${kc.token}`
          }
        }
      );

      if (response.status === 200) {
        setMessage('Product quantity updated successfully.');
        setMessageClass('success');
      } else {
        setMessage('Error updating product quantity.');
        setMessageClass('error');
      }
    } catch (error) {
      setMessage('Error: Unable to update product quantity.');
      setMessageClass('error');
    }
  };

  return (
    <div className="update-inventory">
      <h3>Update Product Quantity in Inventory</h3>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Product Name:</label>
          <input
            type="text"
            value={productName}
            onChange={handleProductNameChange}
            placeholder="Enter product name"
          />
        </div>

        <div>
          <label>Quantity Change:</label>
          <input
            type="number"
            value={quantity}
            onChange={handleQuantityChange}
            placeholder="Enter quantity change (positive or negative)"
          />
        </div>

        <button type="submit">Update Quantity</button>
      </form>

      {message && (
        <p className={messageClass}>{message}</p>
      )}
    </div>
  );
}

export default UpdateInventoryQuantity;
