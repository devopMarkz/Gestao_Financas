<template>
  <div class="dashboard-wrapper">
    <div class="background-pattern"></div>
    
    <!-- Header -->
    <header class="dashboard-header">
      <div class="header-content">
        <div class="header-left">
          <div class="logo">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2V6M12 18V22M4.93 4.93L7.76 7.76M16.24 16.24L19.07 19.07M2 12H6M18 12H22M4.93 19.07L7.76 16.24M16.24 7.76L19.07 4.93" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <circle cx="12" cy="12" r="3" stroke="currentColor" stroke-width="2"/>
            </svg>
            <span>Controle Financeiro</span>
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

    <div class="dashboard-container">
      <div class="dashboard-header-section">
        <h1 class="dashboard-title">Dashboard Financeiro</h1>
        
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
              <label>Data Início</label>
              <input type="date" v-model="dataInicio" class="date-input" />
            </div>
            <div class="filtro-group">
              <label>Data Fim</label>
              <input type="date" v-model="dataFim" class="date-input" />
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
              <label>Descrição</label>
              <input type="text" v-model="filtroDescricao" placeholder="Buscar..." class="text-input" />
            </div>
            <button @click="carregarDados" class="filtrar-button" :disabled="carregando">
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

      <!-- Cards de Resumo -->
      <div class="resumo-grid">
        <div class="resumo-card receitas">
          <div class="card-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2L15.09 8.26L22 9L17 14L18.18 21L12 17.77L5.82 21L7 14L2 9L8.91 8.26L12 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <div class="card-content">
            <h3>Receitas</h3>
            <p class="valor">{{ formatarBRL(resumo.totalReceitas) }}</p>
          </div>
        </div>

        <div class="resumo-card despesas">
          <div class="card-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M16 4H18C18.5304 4 19.0391 4.21071 19.4142 4.58579C19.7893 4.96086 20 5.46957 20 6V18C20 18.5304 19.7893 19.0391 19.4142 19.4142C19.0391 19.7893 18.5304 20 18 20H6C5.46957 20 4.96086 19.7893 4.58579 19.4142C4.21071 19.0391 4 18.5304 4 18V6C4 5.46957 4.21071 4.96086 4.58579 4.58579C4.96086 4.21071 5.46957 4 6 4H8M16 4V2M16 4V6M8 4V2M8 4V6M4 10H20" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <div class="card-content">
            <h3>Despesas</h3>
            <p class="valor">{{ formatarBRL(resumo.totalDespesas) }}</p>
          </div>
        </div>

        <div class="resumo-card saldo" :class="saldoClass">
          <div class="card-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <line x1="12" y1="1" x2="12" y2="23" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M17 5H9.5C8.57174 5 7.6815 5.36875 7.02513 6.02513C6.36875 6.6815 6 7.57174 6 8.5C6 9.42826 6.36875 10.3185 7.02513 10.9749C7.6815 11.6313 8.57174 12 9.5 12H14.5C15.4283 12 16.3185 12.3687 16.9749 13.0251C17.6313 13.6815 18 14.5717 18 15.5C18 16.4283 17.6313 17.3185 16.9749 17.9749C16.3185 18.6313 15.4283 19 14.5 19H6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <div class="card-content">
            <h3>Saldo</h3>
            <p class="valor" :class="saldoClass">{{ formatarBRL(resumo.saldo) }}</p>
          </div>
        </div>
      </div>

      <!-- Transações -->
      <div class="transacoes-card">
        <div class="transacoes-header">
          <h3>Transações ({{ totalTransacoes }} encontradas)</h3>
          
          <div class="header-actions">
            <!-- Ícone de Filtros -->
            <div class="filter-dropdown-wrapper">
              <button class="filter-icon-btn" @click="toggleFiltrosTransacao" :class="{ active: mostrarFiltrosTransacao }">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <polygon points="22,3 2,3 10,12.46 10,19 14,21 14,12.46" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </button>
              
              <transition name="dropdown-slide">
                <div v-show="mostrarFiltrosTransacao" class="filter-dropdown-content">
                  <div class="filter-section">
                    <span class="filter-section-label">Status:</span>
                    <div class="filter-buttons">
                      <button 
                        class="filter-btn" 
                        :class="{ active: filtroStatus === null }" 
                        @click="setFiltroStatus(null)"
                      >
                        Todas
                      </button>
                      <button 
                        class="filter-btn" 
                        :class="{ active: filtroStatus === true }" 
                        @click="setFiltroStatus(true)"
                      >
                        Pagas
                      </button>
                      <button 
                        class="filter-btn" 
                        :class="{ active: filtroStatus === false }" 
                        @click="setFiltroStatus(false)"
                      >
                        Pendentes
                      </button>
                    </div>
                  </div>
                  
                  <div class="filter-section">
                    <span class="filter-section-label">Tipo:</span>
                    <div class="filter-buttons">
                      <button 
                        class="filter-btn" 
                        :class="{ active: filtroTipoTransacao === null }" 
                        @click="setFiltroTipoTransacao(null)"
                      >
                        Todos
                      </button>
                      <button 
                        class="filter-btn" 
                        :class="{ active: filtroTipoTransacao === 'RECEITA' }" 
                        @click="setFiltroTipoTransacao('RECEITA')"
                      >
                        Receitas
                      </button>
                      <button 
                        class="filter-btn" 
                        :class="{ active: filtroTipoTransacao === 'DESPESA' }" 
                        @click="setFiltroTipoTransacao('DESPESA')"
                      >
                        Despesas
                      </button>
                    </div>
                  </div>
                </div>
              </transition>
            </div>
            
            <button class="add-transaction-btn" @click="abrirModalTransacao()">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <line x1="12" y1="5" x2="12" y2="19" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <line x1="5" y1="12" x2="19" y2="12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
              Nova Transação
            </button>
          </div>
        </div>

        <div v-if="transacoes.length" class="transacoes-table">
          <!-- Versão Desktop - Tabela Tradicional -->
          <div class="desktop-table">
            <div class="table-header">
              <div class="th">Descrição</div>
              <div class="th">Valor</div>
              <div class="th">Data</div>
              <div class="th">Categoria</div>
              <div class="th">Status</div>
              <div class="th">Ações</div>
            </div>
            <div class="table-body">
              <div v-for="tx in transacoesFiltradas" :key="tx.id" class="table-row">
                <div class="td">
                  <div>
                    <div class="tx-descricao">{{ tx.descricao }}</div>
                    <div class="tx-observacoes" v-if="tx.observacoes">{{ tx.observacoes }}</div>
                  </div>
                </div>
                <div class="td valor" :class="tx.tipo.toLowerCase()">{{ formatarBRL(tx.valor) }}</div>
                <div class="td">{{ formatarData(tx.dataTransacao) }}</div>
                <div class="td">
                  <span class="categoria-badge">{{ tx.categoria.nome }}</span>
                </div>
                <div class="td">
                  <span class="status-badge" :class="tx.paga ? 'paga' : 'pendente'">
                    {{ tx.paga ? 'Paga' : 'Pendente' }}
                  </span>
                </div>
                <div class="td">
                  <button class="action-btn edit" @click="abrirModalTransacao(tx)">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <path d="M11 4H4C3.46957 4 2.96086 4.21071 2.58579 4.58579C2.21071 4.96086 2 5.46957 2 6V20C2 20.5304 2.21071 21.0391 2.58579 21.4142C2.96086 21.7893 3.46957 22 4 22H18C18.5304 22 19.0391 21.7893 19.4142 21.4142C19.7893 21.0391 20 20.5304 20 20V13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                      <path d="M18.5 2.49998C18.8978 2.10216 19.4374 1.87866 20 1.87866C20.5626 1.87866 21.1022 2.10216 21.5 2.49998C21.8978 2.89781 22.1213 3.43737 22.1213 3.99998C22.1213 4.56259 21.8978 5.10216 21.5 5.49998L12 15L8 16L9 12L18.5 2.49998Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </button>
                  <button class="action-btn delete" @click="confirmarExclusao(tx)">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <polyline points="3,6 5,6 21,6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                      <path d="M19 6V20C19 20.5304 18.7893 21.0391 18.4142 21.4142C18.0391 21.7893 17.5304 22 17 22H7C6.46957 22 5.96086 21.7893 5.58579 21.4142C5.21071 21.0391 5 20.5304 5 20V6M8 6V4C8 3.46957 8.21071 2.96086 8.58579 2.58579C8.96086 2.21071 9.46957 2 10 2H14C14.5304 2 15.0391 2.21071 15.4142 2.58579C15.7893 2.96086 16 3.46957 16 4V6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- Versão Mobile - Lista Compacta Expansível -->
          <div class="mobile-list">
            <div v-for="tx in transacoesFiltradas" :key="tx.id" class="table-row" :class="{ expanded: isTransacaoExpandida(tx.id) }">
              <div class="transaction-compact" @click="toggleTransacao(tx.id)">
                <div class="compact-main">
                  <div class="compact-info">
                    <span class="compact-description">{{ tx.descricao }}</span>
                    <span class="compact-date">{{ formatarData(tx.dataTransacao) }}</span>
                  </div>
                  <div class="compact-right">
                    <span class="compact-value" :class="tx.tipo.toLowerCase()">{{ formatarBRL(tx.valor) }}</span>
                    <span class="compact-status" :class="tx.paga ? 'paga' : 'pendente'">
                      {{ tx.paga ? 'Paga' : 'Pendente' }}
                    </span>
                  </div>
                </div>
                <div class="expand-indicator">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="chevron" :class="{ rotated: isTransacaoExpandida(tx.id) }">
                    <polyline points="6,9 12,15 18,9" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </div>
              </div>

              <!-- Versão Expandida -->
              <transition name="expand">
                <div v-show="isTransacaoExpandida(tx.id)" class="transaction-expanded">
                  <div class="expanded-content">
                    <div class="expanded-row">
                      <div class="expanded-field">
                        <label>Descrição Completa</label>
                        <div class="tx-descricao">{{ tx.descricao }}</div>
                        <div class="tx-observacoes" v-if="tx.observacoes">{{ tx.observacoes }}</div>
                      </div>
                    </div>
                    
                    <div class="expanded-row">
                      <div class="expanded-field">
                        <label>Valor</label>
                        <div class="expanded-value" :class="tx.tipo.toLowerCase()">{{ formatarBRL(tx.valor) }}</div>
                      </div>
                      <div class="expanded-field">
                        <label>Data</label>
                        <div>{{ formatarData(tx.dataTransacao) }}</div>
                      </div>
                    </div>

                    <div class="expanded-row">
                      <div class="expanded-field">
                        <label>Categoria</label>
                        <span class="categoria-badge">{{ tx.categoria.nome }}</span>
                      </div>
                      <div class="expanded-field">
                        <label>Status</label>
                        <span class="status-badge" :class="tx.paga ? 'paga' : 'pendente'">
                          {{ tx.paga ? 'Paga' : 'Pendente' }}
                        </span>
                      </div>
                    </div>

                    <div class="expanded-actions">
                      <button class="action-btn edit" @click.stop="abrirModalTransacao(tx)">
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                          <path d="M11 4H4C3.46957 4 2.96086 4.21071 2.58579 4.58579C2.21071 4.96086 2 5.46957 2 6V20C2 20.5304 2.21071 21.0391 2.58579 21.4142C2.96086 21.7893 3.46957 22 4 22H18C18.5304 22 19.0391 21.7893 19.4142 21.4142C19.7893 21.0391 20 20.5304 20 20V13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                          <path d="M18.5 2.49998C18.8978 2.10216 19.4374 1.87866 20 1.87866C20.5626 1.87866 21.1022 2.10216 21.5 2.49998C21.8978 2.89781 22.1213 3.43737 22.1213 3.99998C22.1213 4.56259 21.8978 5.10216 21.5 5.49998L12 15L8 16L9 12L18.5 2.49998Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                      </button>
                      <button class="action-btn delete" @click.stop="confirmarExclusao(tx)">
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                          <polyline points="3,6 5,6 21,6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                          <path d="M19 6V20C19 20.5304 18.7893 21.0391 18.4142 21.4142C18.0391 21.7893 17.5304 22 17 22H7C6.46957 22 5.96086 21.7893 5.58579 21.4142C5.21071 21.0391 5 20.5304 5 20V6M8 6V4C8 3.46957 8.21071 2.96086 8.58579 2.58579C8.96086 2.21071 9.46957 2 10 2H14C14.5304 2 15.0391 2.21071 15.4142 2.58579C15.7893 2.96086 16 3.46957 16 4V6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                      </button>
                    </div>
                  </div>
                </div>
              </transition>
            </div>
          </div>
        </div>

        <div v-else class="empty-state">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M16 16S14 14 12 14 8 16 8 16" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <line x1="9" y1="9" x2="9.01" y2="9" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <line x1="15" y1="9" x2="15.01" y2="9" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <h4>Nenhuma transação encontrada</h4>
          <p>Comece adicionando sua primeira transação financeira</p>
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

      <div class="acoes-futuras">
        <button class="acao-btn" @click="irParaCategorias">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M20 7L9 18L4 13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          Gerenciar Categorias
        </button>
        <button class="acao-btn" @click="irParaContasRecorrentes">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="3" y="4" width="18" height="18" rx="2" ry="2" stroke="currentColor" stroke-width="2"/>
            <line x1="16" y1="2" x2="16" y2="6" stroke="currentColor" stroke-width="2"/>
            <line x1="8" y1="2" x2="8" y2="6" stroke="currentColor" stroke-width="2"/>
            <line x1="3" y1="10" x2="21" y2="10" stroke="currentColor" stroke-width="2"/>
          </svg>
          Contas Recorrentes
        </button>
        <button class="acao-btn disabled">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" stroke-width="2"/>
            <path d="M12 7V12L15 15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          Orçamento (em breve)
        </button>
      </div>
    </div>

    <!-- Modal de Transação -->
    <div v-if="mostrarModal" class="modal-overlay" @click="fecharModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ transacaoEditando ? 'Editar Transação' : 'Nova Transação' }}</h3>
          <button @click="fecharModal" class="close-modal">×</button>
        </div>
        <form @submit.prevent="salvarTransacao" class="modal-form">
          <div class="form-row">
            <div class="form-group">
              <label>Descrição *</label>
              <input type="text" v-model="formTransacao.descricao" required class="form-input" />
            </div>
            <div class="form-group">
              <label>Valor *</label>
              <input type="number" step="0.01" v-model="formTransacao.valor" required class="form-input" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Data *</label>
              <input type="date" v-model="formTransacao.dataTransacao" required class="form-input" />
            </div>
            <div class="form-group">
              <label>Tipo *</label>
              <select v-model="formTransacao.tipo" required class="form-input">
                <option value="">Selecione</option>
                <option value="RECEITA">Receitas</option>
                <option value="DESPESA">Despesas</option>
              </select>
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Categoria *</label>
              <select v-model="formTransacao.categoriaId" required class="form-input">
                <option value="">Selecione uma categoria</option>
                <option v-for="categoria in categoriasDisponiveis" :key="categoria.id" :value="categoria.id">
                  {{ categoria.nome }} ({{ categoria.tipo }})
                </option>
              </select>
            </div>
            <div class="form-group">
              <label>Status</label>
              <select v-model="formTransacao.paga" class="form-input">
                <option :value="true">Paga</option>
                <option :value="false">Pendente</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label>Observações</label>
            <textarea v-model="formTransacao.observacoes" class="form-input" rows="3"></textarea>
          </div>
          <div class="modal-actions">
            <button type="button" @click="fecharModal" class="btn-cancel">Cancelar</button>
            <button type="submit" :disabled="salvandoTransacao" class="btn-save">
              <span v-if="salvandoTransacao" class="loading-spinner"></span>
              {{ salvandoTransacao ? 'Salvando...' : 'Salvar' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Modal de Confirmação de Exclusão -->
    <div v-if="transacaoParaExcluir" class="modal-overlay" @click="transacaoParaExcluir = null">
      <div class="modal-content confirmation-modal" @click.stop>
        <div class="modal-header">
          <h3>Confirmar Exclusão</h3>
          <button @click="transacaoParaExcluir = null" class="close-modal">×</button>
        </div>
        <div class="confirmation-content">
          <div class="confirmation-icon">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
              <line x1="15" y1="9" x2="9" y2="15" stroke="currentColor" stroke-width="2"/>
              <line x1="9" y1="9" x2="15" y2="15" stroke="currentColor" stroke-width="2"/>
            </svg>
          </div>
          <h4>Tem certeza que deseja excluir esta transação?</h4>
          <div class="transacao-info">
            <p><strong>{{ transacaoParaExcluir.descricao }}</strong></p>
            <p>Valor: <span :class="transacaoParaExcluir.tipo.toLowerCase()">{{ formatarBRL(transacaoParaExcluir.valor) }}</span></p>
            <p>Data: {{ formatarData(transacaoParaExcluir.dataTransacao) }}</p>
            <p>Categoria: {{ transacaoParaExcluir.categoria.nome }}</p>
            <p class="warning-text">⚠️ Esta ação não pode ser desfeita!</p>
          </div>
          <div class="confirmation-actions">
            <button @click="transacaoParaExcluir = null" class="btn-cancel">Cancelar</button>
            <button @click="excluirTransacao" :disabled="carregando" class="btn-delete">
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
import apiService from '../services/apiService';
import authService from '../services/authService';

export default {
  name: 'DashboardView',
  data() {
    return {
      transacoesExpandidas: new Set(),

      mostrarFiltros: false,
      mostrarFiltrosTransacao: false, 

      dataInicio: '',
      dataFim: '',
      filtroTipo: '',
      filtroDescricao: '',
      filtroStatus: null, 
      filtroTipoTransacao: null,
      
      carregando: false,
      erro: '',
      
      resumo: {
        totalReceitas: 0,
        totalDespesas: 0,
        saldo: 0
      },
      transacoes: [], 
      
      paginaAtual: 0,
      tamanhoPagina: 10,
      totalTransacoes: 0,
      totalPaginas: 0,
      
      
      mostrarModal: false,
      transacaoEditando: null,
      salvandoTransacao: false,
      formTransacao: {
        categoriaId: '',
        descricao: '',
        valor: '',
        dataTransacao: '',
        tipo: '',
        observacoes: '',
        paga: true
      },
      categoriasDisponiveis: [],
      transacaoParaExcluir: null,
    };
  },
  computed: {
    saldoClass() {
      return this.resumo.saldo >= 0 ? 'positivo' : 'negativo';
    },
    transacoesFiltradas() {
      return this.transacoes.filter(tx => {
        const statusMatch = this.filtroStatus === null || tx.paga === this.filtroStatus;
        const typeMatch = this.filtroTipoTransacao === null || tx.tipo === this.filtroTipoTransacao;
        return statusMatch && typeMatch;
      });
    }
  },
  watch: {
    'formTransacao.tipo'(novoTipo) {
      if (novoTipo) {
        this.carregarCategorias(novoTipo);
        this.formTransacao.categoriaId = '';
      } else {
        this.carregarCategorias();
        this.formTransacao.categoriaId = '';
      }
    }
  },
  methods: {
    toggleTransacao(transacaoId) {
      if (this.transacoesExpandidas.has(transacaoId)) {
        this.transacoesExpandidas.delete(transacaoId);
      } else {
        this.transacoesExpandidas.add(transacaoId);
      }
      this.transacoesExpandidas = new Set(this.transacoesExpandidas);
    },

    isTransacaoExpandida(transacaoId) {
      return this.transacoesExpandidas.has(transacaoId);
    },


    toggleFiltros() {
      this.mostrarFiltros = !this.mostrarFiltros;
    },

    toggleFiltrosTransacao() {
      this.mostrarFiltrosTransacao = !this.mostrarFiltrosTransacao;
    },

    setFiltroStatus(status) {
      this.filtroStatus = status;
    },

    setFiltroTipoTransacao(tipo) {
      this.filtroTipoTransacao = tipo;
    },

    async carregarDados() {
      this.carregando = true;
      this.erro = '';
      
      try {
        await Promise.all([
          this.carregarResumo(),
          this.carregarTransacoes()
        ]);
      } catch (error) {
        console.error('Erro ao carregar dados:', error);
        this.erro = 'Erro ao carregar dados. Tente novamente.';
      } finally {
        this.carregando = false;
      }
    },

    async carregarResumo() {
      const params = {};
      if (this.dataInicio) params.dataInicio = this.dataInicio;
      if (this.dataFim) params.dataFim = this.dataFim;
      
      this.resumo = await apiService.getResumo(params);
    },

    async carregarTransacoes() {
      const params = {
        pageNumber: this.paginaAtual,
        pageSize: this.tamanhoPagina
      };
      
      if (this.filtroDescricao) params.descricao = this.filtroDescricao;
      if (this.filtroTipo) params.tipo = this.filtroTipo;
      if (this.dataInicio) params.dataMin = this.dataInicio;
      if (this.dataFim) params.dataMax = this.dataFim;
      

      const data = await apiService.getTransacoes(params);
      this.transacoes = data.content; 
      this.totalTransacoes = data.totalElements;
      this.totalPaginas = data.totalPages;
    },

    mudarPagina(novaPagina) {
      this.paginaAtual = novaPagina;
      this.carregarTransacoes();
    },

    abrirModalTransacao(transacao = null) {
      this.transacaoEditando = transacao;
      if (transacao) {
        this.formTransacao = {
          categoriaId: transacao.categoria.id,
          descricao: transacao.descricao,
          valor: transacao.valor,
          dataTransacao: transacao.dataTransacao,
          tipo: transacao.tipo,
          observacoes: transacao.observacoes || '',
          paga: transacao.paga
        };
        this.carregarCategorias(transacao.tipo);
      } else {
        this.formTransacao = {
          categoriaId: '',
          descricao: '',
          valor: '',
          dataTransacao: new Date().toISOString().substr(0, 10),
          tipo: '',
          observacoes: '',
          paga: true
        };
        this.carregarCategorias();
      }
      this.mostrarModal = true;
    },

    fecharModal() {
      this.mostrarModal = false;
      this.transacaoEditando = null;
    },

    async salvarTransacao() {
      this.salvandoTransacao = true;
      try {
        const transacaoData = {
          ...this.formTransacao,
          categoriaId: parseInt(this.formTransacao.categoriaId),
          valor: parseFloat(this.formTransacao.valor)
        };

        if (this.transacaoEditando) {
          await apiService.updateTransacao(this.transacaoEditando.id, transacaoData);
        } else {
          await apiService.createTransacao(transacaoData);
        }

        this.fecharModal();
        await this.carregarDados();
      } catch (error) {
        console.error('Erro ao salvar transação:', error);
        this.erro = 'Erro ao salvar transação. Tente novamente.';
      } finally {
        this.salvandoTransacao = false;
      }
    },

    confirmarExclusao(transacao) {
      this.transacaoParaExcluir = transacao;
    },

    async excluirTransacao() {
      this.carregando = true;
      try {
        await apiService.deleteTransacao(this.transacaoParaExcluir.id);
        this.transacaoParaExcluir = null;
        await this.carregarDados();
      } catch (error) {
        console.error('Erro ao excluir transação:', error);
        this.erro = 'Erro ao excluir transação. Tente novamente.';
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

    formatarData(dataString) {
      const data = new Date(dataString + 'T00:00:00');
      return data.toLocaleDateString('pt-BR');
    },

    definirDatasIniciais() {
      const hoje = new Date();
      const primeiroDia = new Date(hoje.getFullYear(), hoje.getMonth(), 1);
      const ultimoDia = new Date(hoje.getFullYear(), hoje.getMonth() + 1, 0);
      this.dataInicio = primeiroDia.toISOString().substr(0, 10);
      this.dataFim = ultimoDia.toISOString().substr(0, 10);
    },

    logout() {
      authService.logout();
      this.$router.push('/login');
    },

    async carregarCategorias(tipo = null) {
      try {
        const params = { ativa: true, pageSize: 100 };
        if (tipo) {
          params.tipo = tipo;
        }
        const data = await apiService.getCategorias(params);
        this.categoriasDisponiveis = data.content;
      } catch (error) {
        console.error('Erro ao carregar categorias:', error);
      }
    },

    irParaCategorias() {
      this.$router.push('/categorias');
    },

    irParaContasRecorrentes(){
      this.$router.push('/contas-recorrentes');
    }
  },
  
  beforeMount() {
    if (!authService.isAuthenticated()) {
      this.$router.push('/login');
      return;
    }
  },
  
  mounted() {
    this.definirDatasIniciais();
    this.carregarDados();
    this.carregarCategorias();
  }
};
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

/* Header - Mobile First */
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

/* Container - Mobile First */
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

.date-input, .select-input, .text-input {
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

.date-input:focus, .select-input:focus, .text-input:focus {
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

/* Cards de Resumo - Mobile First */
.resumo-grid {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 24px;
}

.resumo-card {
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 12px;
  padding: 20px;
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.2s ease;
}

.resumo-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.card-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.receitas .card-icon {
  background: linear-gradient(135deg, #059669 0%, #10b981 100%);
}

.despesas .card-icon {
  background: linear-gradient(135deg, #dc2626 0%, #ef4444 100%);
}

.saldo .card-icon {
  background: linear-gradient(135deg, #7c3aed 0%, #a855f7 100%);
}

.card-content h3 {
  color: #6b7280;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 4px;
}

.card-content .valor {
  color: #1f2937;
  font-size: 20px;
  font-weight: 700;
  margin: 0;
}

.positivo {
  color: #059669 !important;
}

.negativo {
  color: #dc2626 !important;
}

/* Transações - Mobile First */
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

.filter-icon-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  color: #6b7280;
  cursor: pointer;
  transition: all 0.2s ease;
  backdrop-filter: blur(10px);
}

.filter-icon-btn:hover {
  background: #f1f5f9;
  border-color: #059669;
  color: #059669;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(5, 150, 105, 0.15);
}

.filter-icon-btn.active {
  background: linear-gradient(135deg, #059669 0%, #10b981 100%);
  color: white;
  border-color: #059669;
  box-shadow: 0 2px 8px rgba(5, 150, 105, 0.25);
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
}

@media (min-width: 768px) {
  .transacoes-header h3 {
    font-size: 18px;
  }
}
/* Tabela responsiva - Cards no mobile */
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

.td {
  display: block;
  margin-bottom: 12px;
  position: relative;
}

.td:last-child {
  margin-bottom: 0;
}

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
  content: "Descrição";
}

.td:nth-child(2):before {
  content: "Valor";
}

.td:nth-child(3):before {
  content: "Data";
}

.td:nth-child(4):before {
  content: "Categoria";
}

.td:nth-child(5):before {
  content: "Status";
}

.td:nth-child(6):before {
  content: "Ações";
}

.td:nth-child(6) {
  display: flex;
  gap: 8px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f3f4f6;
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

.td.valor.receita {
  color: #059669;
  font-weight: 600;
  font-size: 18px;
}

.td.valor.despesa {
  color: #dc2626;
  font-weight: 600;
  font-size: 18px;
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

.status-badge.paga {
  background: rgba(5, 150, 105, 0.1);
  color: #059669;
}

.status-badge.pendente {
  background: rgba(245, 158, 11, 0.1);
  color: #d97706;
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

.acoes-futuras {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.acao-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 16px 20px;
  background: #f3f4f6;
  color: #9ca3af;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  cursor: not-allowed;
  font-size: 14px;
  font-weight: 500;
  font-family: inherit;
  text-decoration: none;
  transition: all 0.2s ease;
  justify-content: center;
  min-height: 48px;
}

.acao-btn:not(.disabled) {
  background: linear-gradient(135deg, #059669 0%, #10b981 100%);
  color: white;
  cursor: pointer;
  border: 1px solid #059669;
}

.acao-btn:not(.disabled):hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(5, 150, 105, 0.3);
}

.acao-btn.disabled {
  opacity: 0.7;
}

.acao-btn:hover {
  background: #e5e7eb;
}

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
  border-radius: 4px;
  transition: all 0.2s ease;
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
  width: 50%;
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

.form-input[type="textarea"] {
  resize: vertical;
  min-height: 80px;
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
    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
    gap: 16px;
    align-items: end;
  }
  
  .filtrar-button {
    justify-self: start;
  }
  
  .resumo-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 24px;
    margin-bottom: 32px;
  }
  
  .resumo-card {
    padding: 24px;
  }
  
  .card-content .valor {
    font-size: 24px;
  }
  
  .transacoes-card {
    padding: 24px;
    margin-bottom: 32px;
  }
  
  .transacoes-header {
    display: grid;
    grid-template-columns: 1fr auto auto;
    align-items: center;
    gap: 16px;
  }

  .transacoes-header h3 {
    grid-column: 1 / 2;
  }

  .filter-group-wrapper {
    grid-column: 2 / 3;
    flex-direction: row;
    gap: 8px;
    justify-content: flex-end;
    width: auto;
  }

  .transaction-filters {
    margin-top: 0;
    justify-content: flex-start;
    width: auto;
  }

  .add-transaction-btn {
    grid-column: 3 / 4;
    width: auto;
  }
}

@media (min-width: 1024px) {
  .filtros-content {
    grid-template-columns: repeat(5, 1fr);
  }
  
  .resumo-grid {
    grid-template-columns: repeat(3, 1fr);
  }

  .table-header {
    display: grid;
    grid-template-columns: 2fr 1fr 1fr 1fr 1fr 1fr;
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
    grid-template-columns: 2fr 1fr 1fr 1fr 1fr 1fr;
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

  .td:before {
    display: none;
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
  }

  .action-btn.edit:after,
  .action-btn.delete:after {
    content: none;
  }
}

@media (max-width: 767px) {
  .modal-content {
    width: 100%;
  }

  .td:first-child {
    margin-bottom: 16px;
    padding-bottom: 16px;
    border-bottom: 1px solid #f3f4f6;
  }
  
  .td:first-child:before {
    display: none;
  }
  
  .td:nth-child(2),
  .td:nth-child(3),
  .td:nth-child(4),
  .td:nth-child(5) {
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 8px;
    padding: 12px;
    margin-bottom: 8px;
    width: calc(50% - 4px);
    display: inline-block;
    vertical-align: top;
    height: 80px;
    box-sizing: border-box;
    overflow: hidden;
  }

  .td:nth-child(2) .td:before,
  .td:nth-child(3) .td:before,
  .td:nth-child(4) .td:before,
  .td:nth-child(5) .td:before {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .categoria-badge,
  .status-badge {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 100%;
    display: block;
    font-size: 12px;
  }

  .td:nth-child(2),
  .td:nth-child(3) {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  
  .td:nth-child(6) {
    clear: both;
    width: 100%;
    margin-top: 16px;
    padding-top: 16px;
    border-top: 1px solid #f3f4f6;
  }
}

.desktop-table {
  display: block;
}

.mobile-list {
  display: none;
}

@media (max-width: 767px) {
  .desktop-table {
    display: none;
  }
  
  .mobile-list {
    display: block;
  }

  .table-row.expanded {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  .transaction-compact {
    padding: 16px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: space-between;
    transition: all 0.2s ease;
  }

  .transaction-compact:hover {
    background: #f9fafb;
  }

  .compact-main {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    margin-right: 12px;
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

  .compact-status.paga {
    background: rgba(5, 150, 105, 0.1);
    color: #059669;
  }

  .compact-status.pendente {
    background: rgba(245, 158, 11, 0.1);
    color: #d97706;
  }

  .expand-indicator {
    display: flex;
    align-items: center;
    color: #9ca3af;
  }

  .expand-indicator .chevron {
    transition: transform 0.3s ease;
  }

  .expand-indicator .chevron.rotated {
    transform: rotate(180deg);
  }

  .transaction-expanded {
    border-top: 1px solid #f3f4f6;
    background: #f9fafb;
  }

  .expanded-content {
    padding: 20px;
  }

  .expanded-row {
    display: flex;
    flex-direction: column;
    gap: 16px;
    margin-bottom: 16px;
  }

  .expanded-row:last-child {
    margin-bottom: 0;
  }

  .expanded-field {
    display: flex;
    flex-direction: column;
    gap: 4px;
  }

  .expanded-field label {
    font-size: 12px;
    font-weight: 600;
    color: #6b7280;
    text-transform: uppercase;
    letter-spacing: 0.05em;
  }

  .expanded-value {
    font-weight: 600;
    font-size: 18px;
  }

  .expanded-value.receita {
    color: #059669;
  }

  .expanded-value.despesa {
    color: #dc2626;
  }

  .expanded-actions {
    display: flex;
    gap: 12px;
    margin-top: 20px;
    padding-top: 16px;
    border-top: 1px solid #e5e7eb;
  }

  .expanded-actions .action-btn {
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

  .expanded-actions .action-btn.edit {
    background: rgba(59, 130, 246, 0.1);
    color: #3b82f6;
  }

  .expanded-actions .action-btn.delete {
    background: rgba(220, 38, 38, 0.1);
    color: #dc2626;
  }

  .expanded-actions .action-btn:hover {
    transform: scale(1.02);
  }

  .expand-enter-active,
  .expand-leave-active {
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    transform-origin: top;
  }

  .expand-enter-from {
    opacity: 0;
    transform: scaleY(0.8);
    max-height: 0;
  }

  .expand-leave-to {
    opacity: 0;
    transform: scaleY(0.8);
    max-height: 0;
  }
}

.filter-dropdown-wrapper {
  position: relative;
  display: inline-block;
}

.filter-dropdown-content {
  position: absolute;
  top: 100%;
  right: 100;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  min-width: 280px;
  padding: 16px;
  margin-top: 4px;
}

.filter-section {
  margin-bottom: 16px;
}

.filter-section:last-child {
  margin-bottom: 0;
}

.filter-section-label {
  display: block;
  font-size: 12px;
  font-weight: 600;
  color: #6b7280;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin-bottom: 8px;
}

.filter-buttons {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.filter-btn {
  padding: 6px 12px;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  background: white;
  color: #64748b;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 13px;
  font-weight: 500;
  flex: 1;
  min-width: 70px;
  text-align: center;
}

.filter-btn:hover {
  background: #f1f5f9;
  color: #374151;
}

.filter-btn.active {
  background: linear-gradient(135deg, #059669 0%, #10b981 100%);
  color: white;
  border-color: #059669;
}

.dropdown-slide-enter-active,
.dropdown-slide-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  transform-origin: top right;
}

.dropdown-slide-enter-from {
  opacity: 0;
  transform: translateY(-10px) scale(0.95);
}

.dropdown-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px) scale(0.95);
}

.confirmation-modal {
  max-width: 400px;
  border-radius: 12px;
  margin: auto;
  align-self: center;
}

.confirmation-content {
  padding: 20px;
  text-align: center;
}

.confirmation-icon {
  margin-bottom: 16px;
  color: #dc2626;
}

.confirmation-content h4 {
  color: #374151;
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
}

.transacao-info {
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 20px;
  text-align: left;
}

.transacao-info p {
  margin: 4px 0;
  color: #374151;
}

.transacao-info .receita {
  color: #059669;
  font-weight: 600;
}

.transacao-info .despesa {
  color: #dc2626;
  font-weight: 600;
}

.warning-text {
  color: #dc2626;
  font-weight: 500;
  font-size: 14px;
  margin-top: 8px !important;
}

.confirmation-actions {
  display: flex;
  gap: 12px;
}

.confirmation-actions .btn-cancel,
.confirmation-actions .btn-delete {
  flex: 1;
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