import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../services/api';

const LivroList = () => {
  const [livros, setLivros] = useState([]);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const currentUserId = 1;

  useEffect(() => {
    const fetchLivros = async () => {
      try {
        const response = await api.get('/livros');
        setLivros(response.data);
      } catch (err) {
        setError('Falha ao carregar o acervo de livros.');
      }
    };
    fetchLivros();
  }, []);

  const handleEmprestimo = async (livroId) => {
    if (!currentUserId) {
        setError('Você precisa estar logado para solicitar um empréstimo.');
        return;
    }
    setSuccessMessage('');
    setError('');
    try {
      await api.post('/emprestimos/solicitar', { userId: currentUserId, livroId });
      setSuccessMessage(`Empréstimo do livro ID ${livroId} solicitado com sucesso!`);
    } catch (err) {
      setError(`Falha ao solicitar o empréstimo do livro ID ${livroId}.`);
    }
  };

  const handleReserva = async (livroId) => {
    if (!currentUserId) {
        setError('Você precisa estar logado para fazer uma reserva.');
        return;
    }
    setSuccessMessage('');
    setError('');
    try {
      await api.post('/reservas/solicitar', { userId: currentUserId, livroId });
      setSuccessMessage(`Reserva do livro ID ${livroId} realizada com sucesso! Você será notificado.`);
    } catch (err) {
      setError(`Falha ao solicitar a reserva do livro ID ${livroId}.`);
    }
  };

  return (
    <div>
      <h2>Acervo de Livros</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {successMessage && <p style={{ color: 'green' }}>{successMessage}</p>}
      
      <table>
        <thead>
          <tr>
            <th>Título</th>
            <th>Autor</th>
            <th>Gênero</th>
            <th>Ano</th>
            <th>Disponível</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {livros.map(livro => (
            <tr key={livro.id}>
              <td>{livro.titulo}</td>
              <td>{livro.autor}</td>
              <td>{livro.genero}</td>
              <td>{livro.anoPublicacao}</td>
              <td>{livro.quantidadeDisponivel > 0 ? 'Sim' : 'Não'} ({livro.quantidadeDisponivel})</td>
              <td>
                <Link to={`/livros/edit/${livro.id}`}>Editar (Admin)</Link>
                {livro.quantidadeDisponivel > 0 ? (
                  <button onClick={() => handleEmprestimo(livro.id)}>Solicitar Empréstimo</button>
                ) : (
                  <button onClick={() => handleReserva(livro.id)}>Reservar</button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default LivroList;
