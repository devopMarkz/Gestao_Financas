<template>
  <div class="dashboard-wrapper">
    <div class="background-pattern"></div>
    
    <!-- Header -->
    <header class="dashboard-header">
      <div class="header-content">
        <div class="header-left">
          <div class="logo">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect x="3" y="4" width="18" height="18" rx="2" ry="2" stroke="currentColor" stroke-width="2"/>
              <line x1="16" y1="2" x2="16" y2="6" stroke="currentColor" stroke-width="2"/>
              <line x1="8" y1="2" x2="8" y2="6" stroke="currentColor" stroke-width="2"/>
              <line x1="3" y1="10" x2="21" y2="10" stroke="currentColor" stroke-width="2"/>
            </svg>
            <span>Contas Recorrentes</span>
          </div>
        </div>
        <div class="header-right">
          <button class="user-menu" @click="voltarDashboard">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M19 12H5M12 19L5 12L12 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            Voltar
          </button>
        </div>
      </div>
    </header>

    <div class="dashboard-container">
      <div class="dashboard-header-section">
        <h1 class="dashboard-title">Contas Recorrentes</h1>
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

      <div v-if="sucesso" class="sucesso-alert">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          <polyline points="22,4 12,14.01 9,11.01" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        {{ sucesso }}
        <button @click="sucesso = ''" class="close-error">×</button>
      </div>

      <!-- Contas Recorrentes -->
      <div class="transacoes-card">
        <div class="transacoes-header">
          <h3>Minhas Contas ({{ contasRecorrentes.length }} cadastradas)</h3>
          
          <div class="header-actions">
            <button class="add-transaction-btn" @click="abrirModalConta()">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <line x1="12" y1="5" x2="12" y2="19" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <line x1="5" y1="12" x2="19" y2="12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
              Nova Conta
            </button>
          </div>
        </div>

        <div v-if="contasRecorrentes.length" class="transacoes-table">
          <!-- Versão Desktop - Tabela Tradicional -->
          <div class="desktop-table">
            <div class="table-header">
              <div class="th">Descrição</div>
              <div class="th">Valor</div>
              <div class="th">Vencimento</div>
              <div class="th">Categoria</div>
              <div class="th">Status</div>
              <div class="th">Ações</div>
            </div>
            <div class="table-body">
              <div v-for="conta in contasRecorrentes" :key="conta.id" class="table-row">
                <div class="td">
                  <div>
                    <div class="tx-descricao">{{ conta.descricao }}</div>
                    <div class="tx-observacoes" v-if="conta.observacoes">{{ conta.observacoes }}</div>
                  </div>
                </div>
                <div class="td valor" :class="conta.tipo.toLowerCase()">{{ formatarBRL(conta.valor) }}</div>
                <div class="td">Dia {{ conta.diaVencimento }}</div>
                <div class="td">
                  <span class="categoria-badge">{{ conta.categoriaNome }}</span>
                </div>
                <div class="td">
                  <span class="status-badge" :class="conta.ativa ? 'ativa' : 'inativa'">
                    {{ conta.ativa ? 'Ativa' : 'Inativa' }}
                  </span>
                </div>
                <div class="td">
                  <button class="action-btn create" @click="confirmarCriarTransacao(conta)">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <line x1="12" y1="5" x2="12" y2="19" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                      <line x1="5" y1="12" x2="19" y2="12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </button>
                  <button class="action-btn edit" @click="abrirModalConta(conta)">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <path d="M11 4H4C3.46957 4 2.96086 4.21071 2.58579 4.58579C2.21071 4.96086 2 5.46957 2 6V20C2 20.5304 2.21071 21.0391 2.58579 21.4142C2.96086 21.7893 3.46957 22 4 22H18C18.5304 22 19.0391 21.7893 19.4142 21.4142C19.7893 21.0391 20 20.5304 20 20V13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                      <path d="M18.5 2.49998C18.8978 2.10216 19.4374 1.87866 20 1.87866C20.5626 1.87866 21.1022 2.10216 21.5 2.49998C21.8978 2.89781 22.1213 3.43737 22.1213 3.99998C22.1213 4.56259 21.8978 5.10216 21.5 5.49998L12 15L8 16L9 12L18.5 2.49998Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </button>
                  <button class="action-btn delete" @click="confirmarExclusao(conta)">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <polyline points="3,6 5,6 21,6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                      <path d="M19 6V20C19 20.5304 18.7893 21.0391 18.4142 21.4142C18.0391 21.7893 17.5304 22 17 22H7C6.46957 22 5.96086 21.7893 5.58579 21.4142C5.21071 21.0391 5 20.5304 5 20V6M8 6V4C8 3.46957 8.21071 2.96086 8.58579 2.58579C8.96086 2.21071 9.46957 2 10 2H14C14.5304 2 15.0391 2.21071 15.4142 2.58579C15.7893 2.96086 16 3.46957 16 4V6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- Versão Mobile - Lista Compacta -->
          <div class="mobile-list">
            <div v-for="conta in contasRecorrentes" :key="conta.id" class="table-row">
              <div class="transaction-compact">
                <div class="compact-main">
                  <div class="compact-info">
                    <span class="compact-description">{{ conta.descricao }}</span>
                    <span class="compact-date">Vence dia {{ conta.diaVencimento }}</span>
                  </div>
                  <div class="compact-right">
                    <span class="compact-value" :class="conta.tipo.toLowerCase()">{{ formatarBRL(conta.valor) }}</span>
                    <span class="compact-status" :class="conta.ativa ? 'ativa' : 'inativa'">
                      {{ conta.ativa ? 'Ativa' : 'Inativa' }}
                    </span>
                  </div>
                </div>
              </div>

              <div class="mobile-actions">
                <button class="action-btn create" @click="confirmarCriarTransacao(conta)">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <line x1="12" y1="5" x2="12" y2="19" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <line x1="5" y1="12" x2="19" y2="12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                  Criar Transação
                </button>
                <button class="action-btn edit" @click="abrirModalConta(conta)">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M11 4H4C3.46957 4 2.96086 4.21071 2.58579 4.58579C2.21071 4.96086 2 5.46957 2 6V20C2 20.5304 2.21071 21.0391 2.58579 21.4142C2.96086 21.7893 3.46957 22 4 22H18C18.5304 22 19.0391 21.7893 19.4142 21.4142C19.7893 21.0391 20 20.5304 20 20V13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M18.5 2.49998C18.8978 2.10216 19.4374 1.87866 20 1.87866C20.5626 1.87866 21.1022 2.10216 21.5 2.49998C21.8978 2.89781 22.1213 3.43737 22.1213 3.99998C22.1213 4.56259 21.8978 5.10216 21.5 5.49998L12 15L8 16L9 12L18.5 2.49998Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                  Editar
                </button>
                <button class="action-btn delete" @click="confirmarExclusao(conta)">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <polyline points="3,6 5,6 21,6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M19 6V20C19 20.5304 18.7893 21.0391 18.4142 21.4142C18.0391 21.7893 17.5304 22 17 22H7C6.46957 22 5.96086 21.7893 5.58579 21.4142C5.21071 21.0391 5 20.5304 5 20V6M8 6V4C8 3.46957 8.21071 2.96086 8.58579 2.58579C8.96086 2.21071 9.46957 2 10 2H14C14.5304 2 15.0391 2.21071 15.4142 2.58579C15.7893 2.96086 16 3.46957 16 4V6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                  Excluir
                </button>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="empty-state">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="3" y="4" width="18" height="18" rx="2" ry="2" stroke="currentColor" stroke-width="2"/>
            <line x1="16" y1="2" x2="16" y2="6" stroke="currentColor" stroke-width="2"/>
            <line x1="8" y1="2" x2="8" y2="6" stroke="currentColor" stroke-width="2"/>
            <line x1="3" y1="10" x2="21" y2="10" stroke="currentColor" stroke-width="2"/>
          </svg>
          <h4>Nenhuma conta recorrente cadastrada</h4>
          <p>Comece adicionando suas contas fixas mensais</p>
        </div>
      </div>
    </div>

    <!-- Modal de Conta Recorrente -->
    <div v-if="mostrarModal" class="modal-overlay" @click="fecharModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ contaEditando ? 'Editar Conta Recorrente' : 'Nova Conta Recorrente' }}</h3>
          <button @click="fecharModal" class="close-modal">×</button>
        </div>
        <form @submit.prevent="salvarConta" class="modal-form">
          <div class="form-row">
            <div class="form-group">
              <label>Descrição *</label>
              <input type="text" v-model="formulario.descricao" required class="form-input" placeholder="Ex: Aluguel, Energia, Internet..." />
            </div>
            <div class="form-group">
              <label>Valor *</label>
              <input type="number" min="0.1" step="0.01" v-model="formulario.valor" required class="form-input" placeholder="0,00" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Dia do Vencimento *</label>
              <input type="number" min="1" max="31" v-model="formulario.diaVencimento" required class="form-input" placeholder="Ex: 5, 10, 15..." />
            </div>
            <div class="form-group">
              <label>Tipo *</label>
              <select v-model="formulario.tipo" required class="form-input">
                <option value="">Selecione</option>
                <option value="RECEITA">Receita</option>
                <option value="DESPESA">Despesa</option>
              </select>
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Categoria *</label>
              <select v-model="formulario.categoriaId" required class="form-input">
                <option value="">Selecione uma categoria</option>
                <option v-for="categoria in categoriasDisponiveis" :key="categoria.id" :value="categoria.id">
                  {{ categoria.nome }} ({{ categoria.tipo }})
                </option>
              </select>
            </div>
            <div class="form-group">
              <label>Status</label>
              <select v-model="formulario.ativa" class="form-input">
                <option :value="true">Ativa</option>
                <option :value="false">Inativa</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label>Observações</label>
            <textarea v-model="formulario.observacoes" class="form-input" rows="3" placeholder="Informações adicionais..."></textarea>
          </div>
          <div class="modal-actions">
            <button type="button" @click="fecharModal" class="btn-cancel">Cancelar</button>
            <button type="submit" :disabled="carregando" class="btn-save">
              <span v-if="carregando" class="loading-spinner"></span>
              {{ carregando ? 'Salvando...' : 'Salvar' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Modal de Confirmação para Criar Transação -->
    <div v-if="contaParaTransacao" class="modal-overlay" @click="contaParaTransacao = null">
      <div class="modal-content confirmation-modal" @click.stop>
        <div class="modal-header">
          <h3>Criar Transação</h3>
          <button @click="contaParaTransacao = null" class="close-modal">×</button>
        </div>
        <div class="confirmation-content">
          <div class="confirmation-icon">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M9 12L11 14L15 10M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <h4>Deseja criar uma transação a partir desta conta recorrente?</h4>
          <div class="conta-info">
            <p><strong>{{ contaParaTransacao.descricao }}</strong></p>
            <p>Valor: <span :class="contaParaTransacao.tipo.toLowerCase()">{{ formatarBRL(contaParaTransacao.valor) }}</span></p>
            <p>Categoria: {{ contaParaTransacao.categoriaNome }}</p>
          </div>
          <div class="confirmation-actions">
            <button @click="contaParaTransacao = null" class="btn-cancel">Cancelar</button>
            <button @click="criarTransacaoFromConta" :disabled="carregando" class="btn-save">
              <span v-if="carregando" class="loading-spinner"></span>
              {{ carregando ? 'Criando...' : 'Criar Transação' }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de Confirmação de Exclusão -->
    <div v-if="contaParaExcluir" class="modal-overlay" @click="contaParaExcluir = null">
      <div class="modal-content confirmation-modal" @click.stop>
        <div class="modal-header">
          <h3>Confirmar Exclusão</h3>
          <button @click="contaParaExcluir = null" class="close-modal">×</button>
        </div>
        <div class="confirmation-content">
          <div class="confirmation-icon">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
              <line x1="15" y1="9" x2="9" y2="15" stroke="currentColor" stroke-width="2"/>
              <line x1="9" y1="9" x2="15" y2="15" stroke="currentColor" stroke-width="2"/>
        </svg>
      </div>
      <h4>Tem certeza que deseja excluir esta conta recorrente?</h4>
      <div class="conta-info">
        <p><strong>{{ contaParaExcluir.descricao }}</strong></p>
        <p>Valor: <span :class="contaParaExcluir.tipo.toLowerCase()">{{ formatarBRL(contaParaExcluir.valor) }}</span></p>
        <p>Vencimento: Dia {{ contaParaExcluir.diaVencimento }}</p>
        <p>Categoria: {{ contaParaExcluir.categoriaNome }}</p>
        <p class="warning-text">⚠️ Esta ação não pode ser desfeita!</p>
      </div>
      <div class="confirmation-actions">
        <button @click="contaParaExcluir = null" class="btn-cancel">Cancelar</button>
        <button @click="excluirConta" :disabled="carregando" class="btn-delete">
          <span v-if="carregando" class="loading-spinner"></span>
          {{ carregando ? 'Excluindo...' : 'Excluir' }}
        </button>
      </div>
    </div>
  </div>
</div>
  </div>
</template>

<script>
import apiService from '@/services/apiService';

export default {
  name: 'ContasRecorrentesView',
  data() {
    return {
      formulario: {
        categoriaId: '',
        descricao: '',
        valor: '',
        diaVencimento: '',
        tipo: '',
        observacoes: '',
        ativa: true
      },
      contaEditando: null,
      contaParaTransacao: null,
      categorias: [],
      contasRecorrentes: [],
      mostrarModal: false,
      carregando: false,
      erro: '',
      sucesso: '',
      contaParaExcluir: null, // Já existe, mas vamos garantir que está sendo usada
    }
  },
  computed: {
    categoriasDisponiveis() {
      if (this.formulario.tipo) {
        return this.categorias.filter(cat => cat.tipo === this.formulario.tipo);
      }
      return this.categorias;
    }
  },
  watch: {
    'formulario.tipo'() {
      this.formulario.categoriaId = '';
    }
  },
  methods: {
    async buscarCategorias() {
      try {
        const response = await apiService.getCategorias({ pageSize: 100, ativa: true });
        this.categorias = response.content || response;
      } catch (error) {
        console.error("Erro ao buscar categorias:", error.message);
        this.erro = "Erro ao carregar categorias";
      }
    },

    async buscarContasRecorrentes() {
      try {
        this.carregando = true;
        const response = await apiService.getContasRecorrentes();
        this.contasRecorrentes = response.content || response;
      } catch (error) {
        console.error("Erro ao buscar contas recorrentes:", error.message);
        this.erro = "Erro ao carregar contas recorrentes";
      } finally {
        this.carregando = false;
      }
    },

    abrirModalConta(conta = null) {
      this.contaEditando = conta;
      if (conta) {
        this.formulario = {
          categoriaId: conta.categoriaId || '',
          descricao: conta.descricao,
          valor: conta.valor,
          diaVencimento: conta.diaVencimento,
          tipo: conta.tipo,
          observacoes: conta.observacoes || '',
          ativa: conta.ativa
        };
      } else {
        this.formulario = {
          categoriaId: '',
          descricao: '',
          valor: '',
          diaVencimento: '',
          tipo: '',
          observacoes: '',
          ativa: true
        };
      }
      this.mostrarModal = true;
    },

    fecharModal() {
      this.mostrarModal = false;
      this.contaEditando = null;
    },

    async salvarConta() {
      this.carregando = true;
      this.erro = '';
      
      try {
        const contaData = {
          ...this.formulario,
          categoriaId: parseInt(this.formulario.categoriaId),
          valor: parseFloat(this.formulario.valor),
          diaVencimento: parseInt(this.formulario.diaVencimento)
        };

        if (this.contaEditando) {
          await apiService.updateContaRecorrente(this.contaEditando.id, contaData);
          this.sucesso = 'Conta recorrente atualizada com sucesso!';
        } else {
          await apiService.createContaRecorrente(contaData);
          this.sucesso = 'Conta recorrente criada com sucesso!';
        }

        this.fecharModal();
        await this.buscarContasRecorrentes();
      } catch (error) {
        console.error("Erro ao salvar conta recorrente:", error.message);
        this.erro = "Erro ao salvar conta recorrente";
      } finally {
        this.carregando = false;
      }
    },

    confirmarCriarTransacao(conta) {
      this.contaParaTransacao = conta;
    },

    async criarTransacaoFromConta() {
      this.carregando = true;
      try {
        await apiService.createTransacaoFromContaRecorrente(this.contaParaTransacao.id);
        this.sucesso = 'Transação criada com sucesso!';
        this.contaParaTransacao = null;
      } catch (error) {
        console.error("Erro ao criar transação:", error.message);
        this.erro = "Erro ao criar transação";
      } finally {
        this.carregando = false;
      }
    },

    confirmarExclusao(conta) {
      this.contaParaExcluir = conta;
    },

    async excluirConta() {
      this.carregando = true;
      try {
        await apiService.deleteContaRecorrente(this.contaParaExcluir.id);
        this.sucesso = 'Conta recorrente excluída com sucesso!';
        this.contaParaExcluir = null;
        await this.buscarContasRecorrentes();
      } catch (error) {
        console.error("Erro ao excluir conta:", error.message);
        this.erro = "Erro ao excluir conta recorrente";
      } finally {
        this.carregando = false;
      }
    },

    formatarBRL(valor) {
      return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
      }).format(valor || 0);
    },

    voltarDashboard() {
      this.$router.push('/dashboard');
    }
  },

  created() {
    this.buscarCategorias();
    this.buscarContasRecorrentes();
  }
}
</script>

<style scoped>
* {
  box-sizing: border-box;
}

.dashboard-wrapper {
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

.dashboard-header {
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

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #059669;
  font-weight: 600;
  font-size: 16px;
}

.logo span {
  display: none;
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

.dashboard-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 16px;
  position: relative;
}

.dashboard-header-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 24px;
}

.dashboard-title {
  color: #1e293b;
  font-size: 24px;
  font-weight: 700;
  margin: 0;
  text-align: center;
}

.transacoes-card {
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 24px;
  backdrop-filter: blur(10px);
}

.transacoes-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.transacoes-header h3 {
  color: #374151;
  font-size: 16px;
  font-weight: 600;
  margin: 0;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.add-transaction-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 8px 16px;
  background: linear-gradient(135deg, #059669 0%, #10b981 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
  height: 40px;
  white-space: nowrap;
}

.add-transaction-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.3);
}

.transacoes-table {
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

.transaction-compact {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f3f4f6;
}

.compact-main {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.compact-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
}

.compact-description {
  font-weight: 500;
  color: #374151;
  font-size: 16px;
}

.compact-date {
  font-size: 14px;
  color: #6b7280;
}

.compact-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 4px;
}

.compact-value {
  font-weight: 600;
  font-size: 16px;
}

.compact-value.receita {
  color: #059669;
}

.compact-value.despesa {
  color: #dc2626;
}

.compact-status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.compact-status.ativa {
  background: rgba(5, 150, 105, 0.1);
  color: #059669;
}

.compact-status.inativa {
  background: rgba(156, 163, 175, 0.1);
  color: #6b7280;
}

.mobile-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
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
  min-width: 100px;
}

.action-btn.create {
  background: rgba(5, 150, 105, 0.1);
  color: #059669;
}

.action-btn.edit {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
}

.action-btn.delete {
  background: rgba(220, 38, 38, 0.1);
  color: #dc2626;
}

.action-btn:hover {
  transform: scale(1.02);
}

.desktop-table {
  display: block;
}

.mobile-list {
  display: none;
}

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

.erro-alert, .sucesso-alert {
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  gap: 8px;
  position: relative;
}

.erro-alert {
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.2);
  color: #dc2626;
}

.sucesso-alert {
  background: rgba(34, 197, 94, 0.1);
  border: 1px solid rgba(34, 197, 94, 0.2);
  color: #16a34a;
}

.close-error {
  position: absolute;
  right: 12px;
  background: none;
  border: none;
  color: inherit;
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
  max-height: 80vh;
  overflow-y: auto;
  animation: slideUp 0.3s ease-out;
}

.confirmation-modal {
  max-width: 400px;
  border-radius: 12px;
  margin: auto;
  align-self: center;
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

.form-row {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
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

.confirmation-content {
  padding: 20px;
  text-align: center;
}

.confirmation-icon {
  margin-bottom: 16px;
  color: #059669;
}

.confirmation-content h4 {
  color: #374151;
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
}

.conta-info {
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 20px;
  text-align: left;
}

.conta-info p {
  margin: 4px 0;
  color: #374151;
}

.conta-info .receita {
  color: #059669;
  font-weight: 600;
}

.conta-info .despesa {
  color: #dc2626;
  font-weight: 600;
}

.confirmation-actions {
  display: flex;
  gap: 12px;
}

.confirmation-actions .btn-cancel,
.confirmation-actions .btn-save {
  flex: 1;
}

@media (max-width: 767px) {
  .transacoes-header {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }

  .transacoes-header h3 {
    text-align: center;
  }

  .header-actions {
    justify-content: center;
  }

  .add-transaction-btn {
    padding: 12px 16px;
    font-size: 16px;
    height: 48px;
    flex: 1;
  }

  .desktop-table {
    display: none;
  }
  
  .mobile-list {
    display: block;
  }

  .mobile-actions {
    flex-direction: column;
  }

  .action-btn {
    min-width: auto;
  }
}

@media (min-width: 768px) {
  .header-content {
    padding: 16px 24px;
  }
  
  .logo {
    font-size: 18px;
    gap: 12px;
  }
  
  .logo span {
    display: inline;
  }
  
  .dashboard-container {
    padding: 32px 24px;
  }
  
  .dashboard-header-section {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 32px;
  }
  
  .dashboard-title {
    font-size: 32px;
    text-align: left;
  }
  
  .transacoes-card {
    padding: 24px;
    margin-bottom: 32px;
  }
  
  .transacoes-header h3 {
    font-size: 18px;
  }

  .form-row {
    flex-direction: row;
  }

  .modal-actions {
    flex-direction: row;
  }

  .modal-overlay {
    align-items: center;
    padding: 20px;
  }

  .modal-content {
    border-radius: 12px;
    max-width: 600px;
    width: 100%;
    max-height: 80vh;
  }
}

@media (min-width: 1024px) {
  .table-header {
    display: grid;
    grid-template-columns: 2fr 1fr 1fr 1fr 1fr 1.5fr;
    gap: 16px;
    padding: 16px;
    background: #f8fafc;
    border-bottom: 1px solid #e5e7eb;
    font-weight: 600;
    color: #374151;
    font-size: 14px;
  }

  .th {
    text-align: left;
  }

  .table-body {
    display: block;
  }

  .table-row {
    display: grid;
    grid-template-columns: 2fr 1fr 1fr 1fr 1fr 1.5fr;
    gap: 16px;
    padding: 16px;
    border-bottom: 1px solid #f3f4f6;
    align-items: center;
    background: white;
    border-radius: 0;
    box-shadow: none;
    margin-bottom: 0;
  }

  .table-row:hover {
    background: #f9fafb;
    box-shadow: none;
  }

  .td {
    display: block;
    margin-bottom: 0;
    position: static;
  }

  .td:nth-child(6) {
    display: flex;
    gap: 8px;
    margin-top: 0;
    padding-top: 0;
    border-top: none;
    justify-content: flex-start;
  }

  .action-btn {
    flex: none;
    padding: 8px 12px;
    min-height: auto;
    font-size: 12px;
    min-width: auto;
  }

  .transaction-compact {
    display: none;
  }

  .mobile-actions {
    display: none;
  }

  .tx-descricao {
    font-weight: 500;
    color: #374151;
    font-size: 16px;
  }

  .tx-observacoes {
    font-size: 14px;
    color: #9ca3af;
    margin-top: 4px;
  }

  .valor.receita {
    color: #059669;
    font-weight: 600;
    font-size: 16px;
  }

  .valor.despesa {
    color: #dc2626;
    font-weight: 600;
    font-size: 16px;
  }

  .categoria-badge {
    padding: 6px 12px;
    border-radius: 6px;
    font-size: 14px;
    font-weight: 500;
    background: rgba(99, 102, 241, 0.1);
    color: #6366f1;
    display: inline-block;
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
}

.confirmation-icon {
  margin-bottom: 16px;
  color: #dc2626;
}

.warning-text {
  color: #dc2626;
  font-weight: 500;
  font-size: 14px;
  margin-top: 8px !important;
}

.btn-delete {
  padding: 12px 20px;
  background: linear-gradient(135deg, #dc2626 0%, #ef4444 100%);
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

.btn-delete:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(220, 38, 38, 0.3);
}

.btn-delete:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}
</style>
