import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import api from '../services/api';

const UserDetail = () => {
  const { id } = useParams();
  const [user, setUser] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await api.get(`/users/find/${id}`);
        setUser(response.data);
      } catch (err) {
        setError('Não foi possível obter os detalhes do usuário.');
      }
    };
    fetchUser();
  }, [id]);

  if (!user) return <p>Carregando...</p>;

  return (
    <div>
      <h2>User Details</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <p><strong>ID:</strong> {user.id}</p>
      <p><strong>Nome:</strong> {user.name}</p>
      <p><strong>Email:</strong> {user.email}</p>
      <p><strong>Grupo ID:</strong> {user.groupId}</p>
      <p><strong>Data de registro:</strong> {user.registrationDate}</p>
      <p><strong>Ativo:</strong> {user.active ? 'Yes' : 'No'}</p>
      <a href="/users">Voltar para lista</a>
    </div>
  );
};

export default UserDetail;
