<template>
  <div class="categorias-wrapper">
    <!-- Background pattern -->
    <div class="background-pattern"></div>
    
    <!-- Header -->
    <header class="categorias-header">
      <div class="header-content">
        <div class="header-left">
          <button class="back-btn" @click="voltarDashboard">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M19 12H5M12 19L5 12L12 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            Voltar
          </button>
          <div class="page-title">
            <h1>Categorias</h1>
          </div>
        </div>
        <div class="header-right">
          <button class="user-menu" @click="logout">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M9 21H5C4.46957 21 3.96086 20.7893 3.58579 20.4142C3.21071 20.0391 3 19.5304 3 19V5C3 4.46957 3.21071 3.96086 3.58579 3.58579C3.96086 3.21071 4.46957 3 5 3H9M16 17L21 12L16 7M21 12H9" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            Sair
          </button>
        </div>
      </div>
    </header>

    <div class="categorias-container">
      <div class="categorias-header-section">
        <h1 class="categorias-title">Gerenciar Categorias</h1>
        
        <!-- Botão de Toggle dos Filtros -->
        <button class="filter-toggle-btn" @click="toggleFiltros" :class="{ active: mostrarFiltros }">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <polygon points="22,3 2,3 10,12.46 10,19 14,21 14,12.46" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <span>{{ mostrarFiltros ? 'Ocultar Filtros' : 'Mostrar Filtros' }}</span>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="chevron" :class="{ rotated: mostrarFiltros }">
            <polyline points="6,9 12,15 18,9" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>
      </div>

      <div v-if="erro" class="erro-alert">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
          <line x1="15" y1="9" x2="9" y2="15" stroke="currentColor" stroke-width="2"/>
          <line x1="9" y1="9" x2="15" y2="15" stroke="currentColor" stroke-width="2"/>
        </svg>
        {{ erro }}
        <button @click="erro = ''" class="close-error">×</button>
      </div>

      <!-- Filtros Colapsáveis -->
      <transition name="filter-slide">
        <div v-show="mostrarFiltros" class="filtros-card">
          <h3 class="filtros-title">Filtros</h3>
          <div class="filtros-content">
            <div class="filtro-group">
              <label>Nome da Categoria</label>
              <input type="text" v-model="filtroNome" placeholder="Buscar categoria..." class="text-input" />
            </div>
            <div class="filtro-group">
              <label>Tipo</label>
              <select v-model="filtroTipo" class="select-input">
                <option value="">Todas</option>
                <option value="RECEITA">Receitas</option>
                <option value="DESPESA">Despesas</option>
              </select>
            </div>
            <div class="filtro-group">
              <label>Status</label>
              <select v-model="filtroAtiva" class="select-input">
                <option value="">Todas</option>
                <option value="true">Ativas</option>
                <option value="false">Inativas</option>
              </select>
            </div>
            <button @click="carregarCategorias" class="filtrar-button" :disabled="carregando">
              <span v-if="carregando" class="loading-spinner"></span>
              <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <circle cx="11" cy="11" r="8" stroke="currentColor" stroke-width="2"/>
                <path d="M21 21L16.65 16.65" stroke="currentColor" stroke-width="2"/>
              </svg>
              {{ carregando ? 'Carregando...' : 'Filtrar' }}
            </button>
          </div>
        </div>
      </transition>

      <!-- Categorias -->
      <div class="categorias-card">
        <div class="categorias-header-section-inner">
          <h3>Categorias ({{ totalCategorias }} encontradas)</h3>
          <button class="add-categoria-btn" @click="abrirModalCategoria()">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <line x1="12" y1="5" x2="12" y2="19" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <line x1="5" y1="12" x2="19" y2="12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            Nova Categoria
          </button>
        </div>

        <div v-if="categorias.length" class="categorias-table">
          <div class="table-header">
            <div class="th">Nome</div>
            <div class="th">Tipo</div>
            <div class="th">Status</div>
            <div class="th">Ações</div>
          </div>
          <div class="table-body">
            <div v-for="categoria in categorias" :key="categoria.id" class="table-row">
              <div class="td">
                <div class="categoria-nome">{{ categoria.nome }}</div>
              </div>
              <div class="td">
                <span class="tipo-badge" :class="categoria.tipo.toLowerCase()">
                  {{ categoria.tipo }}
                </span>
              </div>
              <div class="td">
                <span class="status-badge" :class="categoria.ativa ? 'ativa' : 'inativa'">
                  {{ categoria.ativa ? 'Ativa' : 'Inativa' }}
                </span>
              </div>
              <div class="td">
                <button class="action-btn edit" @click="abrirModalCategoria(categoria)">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M11 4H4C3.46957 4 2.96086 4.21071 2.58579 4.58579C2.21071 4.96086 2 5.46957 2 6V20C2 20.5304 2.21071 21.0391 2.58579 21.4142C2.96086 21.7893 3.46957 22 4 22H18C18.5304 22 19.0391 21.7893 19.4142 21.4142C19.7893 21.0391 20 20.5304 20 20V13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M18.5 2.49998C18.8978 2.10216 19.4374 1.87866 20 1.87866C20.5626 1.87866 21.1022 2.10216 21.5 2.49998C21.8978 2.89781 22.1213 3.43737 22.1213 3.99998C22.1213 4.56259 21.8978 5.10216 21.5 5.49998L12 15L8 16L9 12L18.5 2.49998Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </button>
                <button class="action-btn delete" @click="confirmarExclusao(categoria)">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <polyline points="3,6 5,6 21,6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M19 6V20C19 20.5304 18.7893 21.0391 18.4142 21.4142C18.0391 21.7893 17.5304 22 17 22H7C6.46957 22 5.96086 21.7893 5.58579 21.4142C5.21071 21.0391 5 20.5304 5 20V6M8 6V4C8 3.46957 8.21071 2.96086 8.58579 2.58579C8.96086 2.21071 9.46957 2 10 2H14C14.5304 2 15.0391 2.21071 15.4142 2.58579C15.7893 2.96086 16 3.46957 16 4V6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </button>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="empty-state">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M20 7L9 18L4 13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <h4>Nenhuma categoria encontrada</h4>
          <p>Comece criando sua primeira categoria</p>
        </div>

        <!-- Paginação -->
        <div v-if="totalPaginas > 1" class="paginacao">
          <button 
            @click="mudarPagina(paginaAtual - 1)" 
            :disabled="paginaAtual === 0"
            class="pagina-btn"
          >
            Anterior
          </button>
          <span class="pagina-info">
            Página {{ paginaAtual + 1 }} de {{ totalPaginas }}
          </span>
          <button 
            @click="mudarPagina(paginaAtual + 1)" 
            :disabled="paginaAtual === totalPaginas - 1"
            class="pagina-btn"
          >
            Próxima
          </button>
        </div>
      </div>
    </div>

    <!-- Modal de Categoria -->
    <div v-if="mostrarModal" class="modal-overlay" @click="fecharModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ categoriaEditando ? 'Editar Categoria' : 'Nova Categoria' }}</h3>
          <button @click="fecharModal" class="close-modal">×</button>
        </div>
        <form @submit.prevent="salvarCategoria" class="modal-form">
          <div class="form-group">
            <label>Nome da Categoria *</label>
            <input type="text" v-model="formCategoria.nome" required class="form-input" placeholder="Ex: Alimentação, Transporte..." />
          </div>
          <div class="form-group">
            <label>Tipo *</label>
            <select v-model="formCategoria.tipo" required class="form-input">
              <option value="">Selecione o tipo</option>
              <option value="RECEITA">Receita</option>
              <option value="DESPESA">Despesa</option>
            </select>
          </div>
          <div class="modal-actions">
            <button type="button" @click="fecharModal" class="btn-cancel">Cancelar</button>
            <button type="submit" :disabled="salvandoCategoria" class="btn-save">
              <span v-if="salvandoCategoria" class="loading-spinner"></span>
              {{ salvandoCategoria ? 'Salvando...' : 'Salvar' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import apiService from '../services/apiService';
import authService from '../services/authService';

export default {
  name: 'CategoriasView',
  data() {
    return {
      // Controle dos filtros
      mostrarFiltros: false,
      
      // Filtros
      filtroNome: '',
      filtroTipo: '',
      filtroAtiva: 'true', // Por padrão mostrar apenas ativas
      
      // Estados
      carregando: false,
      erro: '',
      
      // Dados
      categorias: [],
      
      // Paginação
      paginaAtual: 0,
      tamanhoPagina: 20,
      totalCategorias: 0,
      totalPaginas: 0,
      
      // Modal
      mostrarModal: false,
      categoriaEditando: null,
      salvandoCategoria: false,
      formCategoria: {
        nome: '',
        tipo: ''
      }
    };
  },
  methods: {
    toggleFiltros() {
      this.mostrarFiltros = !this.mostrarFiltros;
    },

    async carregarCategorias() {
      this.carregando = true;
      this.erro = '';
      
      try {
        const params = {
          pageNumber: this.paginaAtual,
          pageSize: this.tamanhoPagina
        };
        
        // Corrigir o nome do parâmetro para 'nomeCategoria'
        if (this.filtroNome) params.nomeCategoria = this.filtroNome;
        if (this.filtroTipo) params.tipo = this.filtroTipo;
        if (this.filtroAtiva !== '') params.ativa = this.filtroAtiva;
        
        const data = await apiService.getCategorias(params);
        this.categorias = data.content;
        this.totalCategorias = data.totalElements;
        this.totalPaginas = data.totalPages;
      } catch (error) {
        console.error('Erro ao carregar categorias:', error);
        this.erro = 'Erro ao carregar categorias. Tente novamente.';
      } finally {
        this.carregando = false;
      }
    },

    mudarPagina(novaPagina) {
      this.paginaAtual = novaPagina;
      this.carregarCategorias();
    },

    abrirModalCategoria(categoria = null) {
      this.categoriaEditando = categoria;
      if (categoria) {
        this.formCategoria = {
          nome: categoria.nome,
          tipo: categoria.tipo
        };
      } else {
        this.formCategoria = {
          nome: '',
          tipo: ''
        };
      }
      this.mostrarModal = true;
    },

    fecharModal() {
      this.mostrarModal = false;
      this.categoriaEditando = null;
    },

    async salvarCategoria() {
      this.salvandoCategoria = true;
      try {
        if (this.categoriaEditando) {
          await apiService.updateCategoria(this.categoriaEditando.id, this.formCategoria);
        } else {
          await apiService.createCategoria(this.formCategoria);
        }

        this.fecharModal();
        await this.carregarCategorias();
      } catch (error) {
        console.error('Erro ao salvar categoria:', error);
        this.erro = 'Erro ao salvar categoria. Tente novamente.';
      } finally {
        this.salvandoCategoria = false;
      }
    },

    async confirmarExclusao(categoria) {
      if (confirm(`Tem certeza que deseja excluir a categoria "${categoria.nome}"?`)) {
        try {
          await apiService.deleteCategoria(categoria.id);
          await this.carregarCategorias();
        } catch (error) {
          console.error('Erro ao excluir categoria:', error);
          
          // Usar a mensagem do backend se disponível, senão usar mensagem padrão
          if (error.status === 409) {
            this.erro = error.message || `Não é possível excluir a categoria "${categoria.nome}" pois ela possui transações vinculadas.`;
          } else if (error.status === 400) {
            this.erro = error.message || `Erro de validação ao excluir a categoria "${categoria.nome}".`;
          } else if (error.status === 500) {
            this.erro = error.message || `Erro interno do servidor ao excluir a categoria "${categoria.nome}".`;
          } else {
            this.erro = error.message || 'Erro ao excluir categoria. Tente novamente.';
          }
        }
      }
    },

    voltarDashboard() {
      this.$router.push('/dashboard');
    },

    logout() {
      authService.logout();
      this.$router.push('/login');
    }
  },
  
  // Verificar autenticação ao montar o componente
  beforeMount() {
    if (!authService.isAuthenticated()) {
      this.$router.push('/login');
      return;
    }
  },
  
  mounted() {
    this.carregarCategorias();
  }
};
</script>

<style scoped>
/* Mobile-First Approach - Estilos base para mobile */
* {
  box-sizing: border-box;
}

.categorias-wrapper {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  position: relative;
}

.background-pattern {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(circle at 1px 1px, rgba(148, 163, 184, 0.1) 1px, transparent 0);
  background-size: 40px 40px;
  opacity: 0.3;
}

/* Header - Mobile First */
.categorias-header {
  background: rgba(255, 255, 255, 0.95);
  border-bottom: 1px solid rgba(226, 232, 240, 0.8);
  backdrop-filter: blur(10px);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 12px 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  background: transparent;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  color: #64748b;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
}

.back-btn:hover {
  background: #f1f5f9;
  color: #374151;
}

.page-title h1 {
  color: #1e293b;
  font-size: 20px;
  font-weight: 700;
  margin: 0;
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  background: transparent;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  color: #64748b;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 13px;
}

.user-menu:hover {
  background: #f1f5f9;
  color: #374151;
}

/* Container - Mobile First */
.categorias-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 16px;
  position: relative;
}

/* Header Section com Título e Botão de Filtros */
.categorias-header-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 24px;
}

.categorias-title {
  color: #1e293b;
  font-size: 24px;
  font-weight: 700;
  margin: 0;
  text-align: center;
}

/* Botão de Toggle dos Filtros */
.filter-toggle-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px 20px;
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 8px;
  color: #374151;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  font-weight: 500;
  backdrop-filter: blur(10px);
  min-height: 48px;
}

.filter-toggle-btn:hover {
  background: rgba(249, 250, 251, 0.95);
  border-color: #059669;
  color: #059669;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.15);
}

.filter-toggle-btn.active {
  background: linear-gradient(135deg, #059669 0%, #10b981 100%);
  color: white;
  border-color: #059669;
}

.filter-toggle-btn .chevron {
  transition: transform 0.3s ease;
}

.filter-toggle-btn .chevron.rotated {
  transform: rotate(180deg);
}

/* Animações dos Filtros */
.filter-slide-enter-active,
.filter-slide-leave-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  transform-origin: top;
}

.filter-slide-enter-from {
  opacity: 0;
  transform: translateY(-20px) scaleY(0.8);
}

.filter-slide-leave-to {
  opacity: 0;
  transform: translateY(-20px) scaleY(0.8);
}

/* Filtros - Mobile First */
.filtros-card {
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 24px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

.filtros-title {
  color: #374151;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
}

.filtros-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.filtro-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.filtro-group label {
  color: #374151;
  font-size: 14px;
  font-weight: 500;
}

.select-input, .text-input {
  padding: 12px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  background: white;
  color: #1f2937;
  font-size: 16px;
  transition: all 0.2s ease;
  -webkit-appearance: none;
  appearance: none;
}

.select-input:focus, .text-input:focus {
  outline: none;
  border-color: #059669;
  box-shadow: 0 0 0 3px rgba(5, 150, 105, 0.1);
}

.filtrar-button {
  padding: 12px 20px;
  background: linear-gradient(135deg, #059669 0%, #10b981 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-size: 16px;
  min-height: 48px;
}

.filtrar-button:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.3);
}

.filtrar-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.loading-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Categorias - Mobile First */
.categorias-card {
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 24px;
  backdrop-filter: blur(10px);
}

.categorias-header-section-inner {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 16px;
}

.categorias-header-section-inner h3 {
  color: #374151;
  font-size: 16px;
  font-weight: 600;
  margin: 0;
}

.add-categoria-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px 16px;
  background: linear-gradient(135deg, #059669 0%, #10b981 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 16px;
  min-height: 48px;
}

.add-categoria-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.3);
}

/* Tabela responsiva - Cards no mobile */
.categorias-table {
  border-radius: 8px;
  overflow: hidden;
}

.table-header {
  display: none;
}

.table-body {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.table-row {
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: all 0.2s ease;
}

.table-row:hover {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.td {
  display: block;
  margin-bottom: 12px;
  position: relative;
}

.td:last-child {
  margin-bottom: 0;
}

/* Labels para mobile */
.td:before {
  content: attr(data-label);
  font-weight: 600;
  color: #374151;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  display: block;
  margin-bottom: 4px;
}

.td:first-child:before {
  content: "Nome";
}

.td:nth-child(2):before {
  content: "Tipo";
}

.td:nth-child(3):before {
  content: "Status";
}

.td:nth-child(4):before {
  content: "Ações";
}

.td:nth-child(4) {
  display: flex;
  gap: 8px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f3f4f6;
}

.categoria-nome {
  font-weight: 500;
  color: #374151;
  font-size: 16px;
}

.tipo-badge {
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  display: inline-block;
}

.tipo-badge.receita {
  background: rgba(5, 150, 105, 0.1);
  color: #059669;
}

.tipo-badge.despesa {
  background: rgba(220, 38, 38, 0.1);
  color: #dc2626;
}

.status-badge {
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  display: inline-block;
}

.status-badge.ativa {
  background: rgba(5, 150, 105, 0.1);
  color: #059669;
}

.status-badge.inativa {
  background: rgba(156, 163, 175, 0.1);
  color: #6b7280;
}

.action-btn {
  flex: 1;
  padding: 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  font-size: 14px;
  font-weight: 500;
  min-height: 44px;
}

.action-btn.edit {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
}

.action-btn.edit:after {
  content: "Editar";
}

.action-btn.delete {
  background: rgba(220, 38, 38, 0.1);
  color: #dc2626;
}

.action-btn.delete:after {
  content: "Excluir";
}

.action-btn:hover {
  transform: scale(1.02);
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 48px 24px;
  color: #9ca3af;
}

.empty-state svg {
  margin-bottom: 16px;
}

.empty-state h4 {
  color: #6b7280;
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
}

.empty-state p {
  color: #9ca3af;
  font-size: 14px;
}

/* Paginação */
.paginacao {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-top: 24px;
  flex-wrap: wrap;
}

.pagina-btn {
  padding: 10px 16px;
  background: #f3f4f6;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  color: #374151;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
  min-height: 44px;
}

.pagina-btn:hover:not(:disabled) {
  background: #e5e7eb;
}

.pagina-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagina-info {
  color: #6b7280;
  font-size: 14px;
  text-align: center;
}

/* Erro Alert */
.erro-alert {
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.2);
  color: #dc2626;
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  gap: 8px;
  position: relative;
}

.close-error {
  position: absolute;
  right: 12px;
  background: none;
  border: none;
  color: #dc2626;
  cursor: pointer;
  font-size: 18px;
  font-weight: bold;
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* Modal Styles - Mobile First */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: flex-end;
  justify-content: center;
  z-index: 1000;
  padding: 0;
}

.modal-content {
  background: white;
  border-radius: 12px 12px 0 0;
  width: 100%;
  max-height: 90vh;
  overflow-y: auto;
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e5e7eb;
  position: sticky;
  top: 0;
  background: white;
  z-index: 10;
}

.modal-header h3 {
  color: #1f2937;
  font-size: 18px;
  font-weight: 600;
  margin: 0;
}

.close-modal {
  background: none;
  border: none;
  font-size: 24px;
  color: #6b7280;
  cursor: pointer;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.close-modal:hover {
  background: #f3f4f6;
  color: #374151;
}

.modal-form {
  padding: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-bottom: 16px;
}

.form-group label {
  color: #374151;
  font-size: 14px;
  font-weight: 500;
}

.form-input {
  padding: 12px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  background: white;
  color: #1f2937;
  font-size: 16px;
  transition: all 0.2s ease;
  -webkit-appearance: none;
  appearance: none;
}

.form-input:focus {
  outline: none;
  border-color: #059669;
  box-shadow: 0 0 0 3px rgba(5, 150, 105, 0.1);
}

.modal-actions {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #e5e7eb;
}

.btn-cancel {
  padding: 12px 20px;
  background: #f3f4f6;
  color: #374151;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 16px;
  min-height: 48px;
}

.btn-cancel:hover {
  background: #e5e7eb;
}

.btn-save {
  padding: 12px 20px;
  background: linear-gradient(135deg, #059669 0%, #10b981 100%);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-size: 16px;
  min-height: 48px;
}

.btn-save:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.3);
}

.btn-save:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

/* Tablet Styles */
@media (min-width: 768px) {
  .header-content {
    padding: 16px 24px;
  }
  
  .page-title h1 {
    font-size: 24px;
  }
  
  .categorias-container {
    padding: 32px 24px;
  }
  
  .categorias-header-section {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 32px;
  }
  
  .categorias-title {
    font-size: 32px;
    text-align: left;
  }
  
  .filter-toggle-btn {
    width: auto;
    min-height: auto;
  }
  
  .filtros-card {
    padding: 24px;
    margin-bottom: 32px;
  }
  
  .filtros-content {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 16px;
    align-items: end;
  }
  
  .filtrar-button {
    justify-self: start;
  }
  
  .categorias-card {
    padding: 24px;
    margin-bottom: 32px;
  }
  
  .categorias-header-section-inner {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .categorias-header-section-inner h3 {
    font-size: 18px;
  }
  
  .add-categoria-btn {
    width: auto;
    font-size: 14px;
    padding: 8px 16px;
    min-height: auto;
  }
  
  /* Tabela desktop */
  .table-header {
    display: grid;
    grid-template-columns: 2fr 1fr 1fr 120px;
    background: #f9fafb;
    border-bottom: 1px solid #e5e7eb;
  }
  
  .th {
    padding: 12px 16px;
    font-weight: 600;
    color: #374151;
    font-size: 14px;
    display: block;
  }
  
  .table-body {
    gap: 0;
  }
  
  .table-row {
    display: grid;
    grid-template-columns: 2fr 1fr 1fr 120px;
    border-bottom: 1px solid #f3f4f6;
    border-radius: 0;
    padding: 0;
    margin-bottom: 0;
    box-shadow: none;
    background: transparent;
  }
  
  .table-row:hover {
    background: #f9fafb;
    box-shadow: none;
  }
  
  .td {
    padding: 12px 16px;
    color: #6b7280;
    font-size: 14px;
    display: flex;
    align-items: center;
    margin-bottom: 0;
    border-top: none;
  }
  
  .td:before {
    display: none;
  }
  
  .td:nth-child(4) {
    margin-top: 0;
    padding-top: 12px;
    border-top: none;
  }
  
  .action-btn {
    flex: none;
    padding: 6px;
    margin-right: 4px;
    min-height: auto;
  }
  
  .action-btn:after {
    display: none;
  }
  
  .modal-overlay {
    align-items: center;
    padding: 20px;
  }
  
  .modal-content {
    border-radius: 12px;
    max-width: 500px;
    animation: none;
  }
  
  .modal-actions {
    flex-direction: row;
    justify-content: flex-end;
  }
  
  .btn-cancel, .btn-save {
    min-height: auto;
    font-size: 14px;
  }
}

/* Grid 2x2 apenas para mobile - após o nome da categoria */
@media (max-width: 767px) {
  .td:first-child {
    margin-bottom: 16px;
    padding-bottom: 16px;
    border-bottom: 1px solid #f3f4f6;
  }
  
  .td:first-child:before {
    display: none;
  }
  
  /* Container para o grid 2x2 - apenas Tipo e Status */
  .td:nth-child(2),
  .td:nth-child(3) {
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 8px;
    padding: 12px;
    margin-bottom: 8px;
    width: calc(50% - 4px);
    display: inline-block;
    vertical-align: top;
    height: 70px; /* Altura fixa */
    box-sizing: border-box;
    overflow: hidden; /* Esconder conteúdo que exceder */
  }

  .td:nth-child(2) { margin-right: 8px; }
  .td:nth-child(3) { margin-left: 0; }

  /* Controlar o texto dentro dos cards */
  .td:nth-child(2):before,
  .td:nth-child(3):before {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  /* Controlar o conteúdo dos badges */
  .tipo-badge,
  .status-badge {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 100%;
    display: block;
  }
  
  .td:nth-child(4) {
    clear: both;
    width: 100%;
    margin-top: 16px;
    padding-top: 16px;
    border-top: 1px solid #f3f4f6;
  }
}
</style>
