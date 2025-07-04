const BASE_URL = "http://localhost:8081"

export default {
  // Auth
  login: `${BASE_URL}/auth/login`,

  usuarios: `${BASE_URL}/usuarios`,

  // Dashboard
  resumo: `${BASE_URL}/dashboard/resumo`,

  // Transações
  transacoes: `${BASE_URL}/transacoes`,
  transacao: (id) => `${BASE_URL}/transacoes/${id}`,
  categoria: (id) => `${BASE_URL}/categorias/${id}`,

  // Categorias (para futuro uso)
  categorias: `${BASE_URL}/categorias`,
}
