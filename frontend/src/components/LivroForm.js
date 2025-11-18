import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';

const LivroForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [livro, setLivro] = useState({
    id: '',
    titulo: '',
    autor: '',
    genero: '',
    anoPublicacao: '',
    quantidadeDisponivel: 0,
    sinopse: ''
  });
  const [error, setError] = useState('');

  useEffect(() => {
    if (id) {
      const fetchLivro = async () => {
        try {
          const response = await api.get(`/livros/find/${id}`);
          setLivro(response.data);
        } catch (err) {
          setError('Falha ao buscar detalhes do livro.');
        }
      };
      fetchLivro();
    }
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setLivro({
      ...livro,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) {
        await api.put('/livros', livro);
      } else {
        await api.post('/livros', livro);
      }
      navigate('/livros');
    } catch (err) {
      setError(`Falha ao ${id ? 'atualizar' : 'cadastrar'} o livro.`);
    }
  };

  return (
    <div>
      <h2>{id ? 'Editar Livro' : 'Cadastrar Novo Livro'}</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Título:</label>
          <input
            type="text"
            name="titulo"
            value={livro.titulo}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Autor:</label>
          <input
            type="text"
            name="autor"
            value={livro.autor}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Gênero:</label>
          <input
            type="text"
            name="genero"
            value={livro.genero}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Ano de Publicação:</label>
          <input
            type="number"
            name="anoPublicacao"
            value={livro.anoPublicacao}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Quantidade Disponível:</label>
          <input
            type="number"
            name="quantidadeDisponivel"
            value={livro.quantidadeDisponivel}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Sinopse:</label>
          <textarea
            name="sinopse"
            value={livro.sinopse}
            onChange={handleChange}
            rows="5"
          />
        </div>
        {error && <p style={{ color: 'red' }}>{error}</p>}
        <button type="submit">{id ? 'Salvar Alterações' : 'Cadastrar Livro'}</button>
      </form>
      <a href="/livros">Voltar para o Acervo</a>
    </div>
  );
};

export default LivroForm;
