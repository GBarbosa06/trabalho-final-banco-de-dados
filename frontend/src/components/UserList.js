import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../services/api';

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await api.get('/users');
        setUsers(response.data);
      } catch (err) {
        setError('Failed to fetch users.');
      }
    };
    fetchUsers();
  }, []);

  const handleDelete = async (id) => {
    try {
      await api.delete(`/users/delete/${id}`);
      setUsers(users.filter(user => user.id !== id));
    } catch (err) {
      setError('Failed to delete user.');
    }
  };

  return (
    <div>
      <h2>Lista de Usuários</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <Link to="/register">Adicionar Novo Usuário</Link>
      <ul>
        {users.map(user => (
          <li key={user.id}>
            {user.name} ({user.email})
            <Link to={`/users/${user.id}`}>Ver</Link>
            <Link to={`/users/edit/${user.id}`}>Editar</Link>
            <button onClick={() => handleDelete(user.id)}>Excluir</button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default UserList;
