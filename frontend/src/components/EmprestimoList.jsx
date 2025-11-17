import React, { useState, useEffect } from 'react';
import api from '../services/api';

const EmprestimoList = () => {
  const [emprestimos, setEmprestimos] = useState([]);
  const [error, setError] = useState('');
  const currentUserId = 1

  useEffect(() => {
    if (currentUserId) {
        const fetchEmprestimos = async () => {
            try {
                const response = await api.get(`/emprestimos/user/${currentUserId}`);
                setEmprestimos(response.data);
            } catch (err) {
                setError('Falha ao carregar seu histórico de empréstimos.');
            }
        };
        fetchEmprestimos();
    } else {
        setError('Você precisa estar logado para ver seus empréstimos.');
    }
  }, [currentUserId]);

  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
  };

  return (
    <div>
      <h2>Meus Empréstimos</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      
      {emprestimos.length === 0 && !error ? (
        <p>Nenhum empréstimo encontrado.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Livro</th>
              <th>Data Retirada</th>
              <th>Prazo Devolução</th>
              <th>Data Devolução</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {emprestimos.map(e => (
              <tr key={e.id}>
                <td>{e.livro.titulo}</td> 
                <td>{formatDate(e.dataRetirada)}</td>
                <td>{formatDate(e.prazoDevolucao)}</td>
                <td>{e.dataDevolucao ? formatDate(e.dataDevolucao) : 'Ativo'}</td>
                <td>{e.dataDevolucao ? 'Devolvido' : 'Em Andamento'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      <a href="/livros">Voltar para o Acervo</a>
    </div>
  );
};

export default EmprestimoList;
