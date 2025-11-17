import React, { useState, useEffect } from 'react';
import api from '../services/api';

const ReservaList = () => {
  const [reservas, setReservas] = useState([]);
  const [error, setError] = useState('');
  const currentUserId = 1;

  useEffect(() => {
    if (currentUserId) {
        const fetchReservas = async () => {
            try {
                const response = await api.get(`/reservas/user/${currentUserId}`);
                setReservas(response.data);
            } catch (err) {
                setError('Falha ao carregar seu histórico de reservas.');
            }
        };
        fetchReservas();
    } else {
        setError('Você precisa estar logado para ver suas reservas.');
    }
  }, [currentUserId]);

  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
  };

  return (
    <div>
      <h2>Minhas Reservas</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      
      {reservas.length === 0 && !error ? (
        <p>Nenhuma reserva encontrada.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Livro</th>
              <th>Data Reserva</th>
              <th>Posição na Fila</th>
              <th>Disponível para Retirada</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {reservas.map(r => (
              <tr key={r.id}>
                <td>{r.livro.titulo}</td> 
                <td>{formatDate(r.dataReserva)}</td>
                <td>{r.posicaoFila}</td> 
                <td>{r.disponivelParaRetirada ? 'Sim' : 'Não'}</td>
                <td>
                    {r.disponivelParaRetirada 
                        ? <strong style={{ color: 'blue' }}>Pronto para Retirada!</strong> 
                        : (r.cancelada ? 'Cancelada' : 'Aguardando')}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      <a href="/livros">Voltar para o Acervo</a>
    </div>
  );
};

export default ReservaList;
