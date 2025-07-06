<template>
  <div class="dashboard-wrapper">
    <div class="background-pattern"></div>
    
    <!-- Header -->
    <header class="dashboard-header">
      <div class="header-content">
        <div class="header-left">
          <div class="logo">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M3 3V21H21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M9 9L12 6L16 10L21 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <span>Receita vs Despesa</span>
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
        <h1 class="dashboard-title">Receita vs Despesa ao Longo do Tempo</h1>
        
        <!-- Seletor de Período Simplificado -->
        <div class="period-selector">
          <button 
            v-for="periodo in periodos" 
            :key="periodo.value"
            @click="selecionarPeriodo(periodo.value)"
            :class="['period-btn', { active: periodoSelecionado === periodo.value }]"
          >
            {{ periodo.label }}
          </button>
          <!--<button 
            @click="ativarPersonalizado"
            :class="['period-btn', { active: mostrarPersonalizado }]"
          >
            Personalizado
          </button>-->
        </div>

        <!-- Período Personalizado -->
        <!--<div v-if="mostrarPersonalizado" class="periodo-personalizado">
          <div class="data-inputs">
            <div class="input-group">
              <label>Data Início</label>
              <input 
                type="date" 
                v-model="dataInicio" 
                class="date-input"
              />
            </div>
            <div class="input-group">
              <label>Data Fim</label>
              <input 
                type="date" 
                v-model="dataFim" 
                class="date-input"
              />
            </div>
            <button @click="aplicarPeriodoPersonalizado" class="btn-aplicar">
              Aplicar
            </button>
          </div>
        </div>-->
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

      <!-- Debug Info 
      <div v-if="debug" class="debug-info">
        <p><strong>Parâmetros:</strong> {{ JSON.stringify(ultimosParametros) }}</p>
        <p><strong>Total Transações:</strong> {{ totalTransacoes }}</p>
        <p><strong>Dados Processados:</strong> {{ dadosGrafico.length }} meses</p>
      </div>-->

      <!-- Cards de Resumo -->
      <div class="resumo-cards">
        <div class="resumo-card receita">
          <div class="card-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2L15.09 8.26L22 9L17 14L18.18 21L12 17.77L5.82 21L7 14L2 9L8.91 8.26L12 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <div class="card-content">
            <h3>Receita Total</h3>
            <p class="valor">{{ formatarBRL(resumo.receitaTotal) }}</p>
            <span class="periodo-info">{{ periodoTexto }}</span>
          </div>
        </div>

        <div class="resumo-card despesa">
          <div class="card-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M17 3L22 8L12 18L2 8L7 3L12 8L17 3Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <div class="card-content">
            <h3>Despesa Total</h3>
            <p class="valor">{{ formatarBRL(resumo.despesaTotal) }}</p>
            <span class="periodo-info">{{ periodoTexto }}</span>
          </div>
        </div>

        <div class="resumo-card saldo" :class="{ positivo: resumo.saldo >= 0, negativo: resumo.saldo < 0 }">
          <div class="card-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 1V23M17 5H9.5C8.57174 5 7.6815 5.36875 7.02513 6.02513C6.36875 6.6815 7.57174 6 8.5C6 9.42826 6.36875 10.3185 7.02513 10.9749C7.6815 11.6312 8.57174 12 9.5 12H14.5C15.4283 12 16.3185 12.3687 16.9749 13.0251C17.6312 13.6815 18 14.5717 18 15.5C18 16.4283 17.6312 17.3185 16.9749 17.9749C16.3185 18.6312 15.4283 19 14.5 19H6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <div class="card-content">
            <h3>Saldo</h3>
            <p class="valor">{{ formatarBRL(resumo.saldo) }}</p>
            <span class="periodo-info">{{ periodoTexto }}</span>
          </div>
        </div>
      </div>

      <!-- Gráfico -->
      <div class="grafico-card">
        <div class="grafico-header">
          <h3>Evolução Mensal</h3>
          <div class="legenda">
            <div class="legenda-item receita">
              <div class="cor-indicador"></div>
              <span>Receitas</span>
            </div>
            <div class="legenda-item despesa">
              <div class="cor-indicador"></div>
              <span>Despesas</span>
            </div>
            <div class="legenda-item saldo">
              <div class="cor-indicador"></div>
              <span>Saldo</span>
            </div>
          </div>
        </div>

        <div v-if="carregando" class="loading-container">
          <div class="loading-spinner"></div>
          <p>Carregando dados...</p>
        </div>

        <div v-else-if="dadosGrafico.length === 0" class="empty-state">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M3 3V21H21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M9 9L12 6L16 10L21 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <h4>Nenhum dado encontrado</h4>
          <p>Não há transações para o período selecionado</p>
        </div>

        <div v-else class="grafico-container">
          <canvas ref="graficoCanvas" class="grafico-canvas"></canvas>
        </div>
      </div>

      <!-- Tabela de Dados -->
      <div class="dados-card">
        <div class="dados-header">
          <h3>Dados Detalhados</h3>
          <!--<button @click="debug = !debug" class="btn-debug">
            {{ debug ? 'Ocultar' : 'Debug' }}
          </button>-->
        </div>
        
        <div v-if="dadosGrafico.length > 0" class="dados-table">
          <div class="table-header">
            <div class="th">Mês</div>
            <div class="th">Receitas</div>
            <div class="th">Despesas</div>
            <div class="th">Saldo</div>
            <div class="th">Variação</div>
          </div>
          <div class="table-body">
            <div class="table-row" v-for="(item, index) in dadosGrafico" :key="item.mes">
              <div class="td" data-label="Mês">{{ item.mesFormatado }}</div>
              <div class="td receita" data-label="Receitas">{{ formatarBRL(item.receita) }}</div>
              <div class="td despesa" data-label="Despesas">{{ formatarBRL(item.despesa) }}</div>
              <div class="td saldo" :class="{ positivo: item.saldo >= 0, negativo: item.saldo < 0 }" data-label="Saldo">
                {{ formatarBRL(item.saldo) }}
              </div>
              <div class="td variacao" :class="{ positivo: item.variacao >= 0, negativo: item.variacao < 0 }" data-label="Variação">
                <span v-if="index > 0">
                  {{ item.variacao >= 0 ? '+' : '' }}{{ formatarBRL(item.variacao) }}
                  <small>({{ item.variacao >= 0 ? '+' : '' }}{{ item.percentualVariacao }}%)</small>
                </span>
                <span v-else class="sem-variacao">-</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import apiService from '@/services/apiService';

export default {
  name: 'GraficoReceitaDespesaView',
  data() {
    return {
      periodoSelecionado: '3',
      mostrarPersonalizado: false,
      dataInicio: '',
      dataFim: '',
      periodos: [
        { value: '3', label: '3 meses' },
        { value: '6', label: '6 meses' },
        { value: '12', label: '12 meses' }
      ],
      dadosGrafico: [],
      resumo: {
        receitaTotal: 0,
        despesaTotal: 0,
        saldo: 0
      },
      carregando: false,
      erro: '',
      debug: false,
      ultimosParametros: {},
      totalTransacoes: 0
    }
  },
  computed: {
    periodoTexto() {
      if (this.mostrarPersonalizado && this.dataInicio && this.dataFim) {
        return `${this.formatarData(this.dataInicio)} até ${this.formatarData(this.dataFim)}`;
      }
      return `Últimos ${this.periodoSelecionado} meses`;
    }
  },
  methods: {
    async buscarDados() {
      this.carregando = true;
      this.erro = '';
      
      try {
        let dataMin, dataMax;
        
        if (this.mostrarPersonalizado && this.dataInicio && this.dataFim) {
          dataMin = this.dataInicio;
          dataMax = this.dataFim;
        } else {
          const hoje = new Date();
          const inicio = new Date();
          inicio.setMonth(inicio.getMonth() - parseInt(this.periodoSelecionado));
          
          dataMin = inicio.toISOString().split('T')[0];
          dataMax = hoje.toISOString().split('T')[0];
        }
        
        console.log('🔍 Teste 1: Buscando TODAS as transações...');
        const todasTransacoes = await apiService.getTransacoes({ pageSize: 1000 });
        console.log('📊 Total de transações no sistema:', todasTransacoes);
        
        const params = {
          dataMin: dataMin,
          dataMax: dataMax,
          pageSize: 1000
        };
        
        this.ultimosParametros = params;
        
        
        const response = await apiService.getTransacoes(params);
        let transacoes = [];
                
        if (response && response.content) {
          transacoes = response.content;
        } else if (Array.isArray(response)) {
          transacoes = response;
        } else {
          console.log('⚠️ Formato de resposta inesperado:', typeof response);
        }
        
        this.totalTransacoes = transacoes.length;
        
        if (transacoes.length > 0) {
          console.log('📋 Primeira transação:', transacoes[0]);
          console.log('📋 Tipos de transação encontrados:', [...new Set(transacoes.map(t => t.tipo))]);
          console.log('📋 Datas das transações:', transacoes.map(t => t.data).slice(0, 5));
        }
        
        this.processarDados(transacoes, dataMin, dataMax);
        
        this.$nextTick(() => {
          this.criarGrafico();
        });
        
      } catch (error) {
        console.error("❌ Erro ao buscar dados:", error);
        this.erro = `Erro ao carregar dados: ${error.message}`;
      } finally {
        this.carregando = false;
      }
    },

    processarDados(transacoes, dataMin, dataMax) {
      console.log('🔄 Processando dados...');
      
      const dadosPorMes = {};
      const inicio = new Date(dataMin);
      const fim = new Date(dataMax);
      
      const dataAtual = new Date(inicio);
      while (dataAtual <= fim) {
        const chave = `${dataAtual.getFullYear()}-${String(dataAtual.getMonth() + 1).padStart(2, '0')}`;
        dadosPorMes[chave] = {
          mes: chave,
          receita: 0,
          despesa: 0,
          saldo: 0,
          mesFormatado: this.formatarMes(chave)
        };
        dataAtual.setMonth(dataAtual.getMonth() + 1);
      }
      
      let receitaTotal = 0;
      let despesaTotal = 0;
      
      transacoes.forEach(transacao => {

        const data = new Date(transacao.dataTransacao);
        const chave = `${data.getFullYear()}-${String(data.getMonth() + 1).padStart(2, '0')}`;
        
        if (dadosPorMes[chave]) {
          const valor = parseFloat(transacao.valor) || 0;
          
          if (transacao.tipo === 'RECEITA') {
            dadosPorMes[chave].receita += valor;
            receitaTotal += valor;
          } else if (transacao.tipo === 'DESPESA') {
            dadosPorMes[chave].despesa += valor;
            despesaTotal += valor;
          }
        }
      });
      
      Object.values(dadosPorMes).forEach(item => {
        item.saldo = item.receita - item.despesa;
      });
      
      this.dadosGrafico = Object.values(dadosPorMes)
        .sort((a, b) => a.mes.localeCompare(b.mes));
      
      this.dadosGrafico.forEach((item, index) => {
        if (index > 0) {
          const anterior = this.dadosGrafico[index - 1];
          item.variacao = item.saldo - anterior.saldo;
          item.percentualVariacao = anterior.saldo !== 0 
            ? ((item.variacao / Math.abs(anterior.saldo)) * 100).toFixed(1)
            : item.saldo !== 0 ? '100.0' : '0.0';
        } else {
          item.variacao = 0;
          item.percentualVariacao = '0.0';
        }
      });
      
      this.resumo = {
        receitaTotal,
        despesaTotal,
        saldo: receitaTotal - despesaTotal
      };
      
      console.log('✅ Dados processados:', {
        meses: this.dadosGrafico.length,
        resumo: this.resumo,
        primeiroMes: this.dadosGrafico[0]
      });
    },

    formatarMes(mesAno) {
      const [ano, mes] = mesAno.split('-');
      const meses = [
        'Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun',
        'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'
      ];
      return `${meses[parseInt(mes) - 1]}/${ano}`;
    },

    formatarData(data) {
      return new Date(data).toLocaleDateString('pt-BR');
    },

    criarGrafico() {
      const canvas = this.$refs.graficoCanvas;
      if (!canvas || this.dadosGrafico.length === 0) return;
      
      const ctx = canvas.getContext('2d');
      const rect = canvas.parentElement.getBoundingClientRect();
      
      canvas.width = rect.width * 2;
      canvas.height = 400 * 2;
      canvas.style.width = rect.width + 'px';
      canvas.style.height = '400px';
      
      ctx.scale(2, 2);
      
      const width = rect.width;
      const height = 400;
      const padding = { top: 40, right: 40, bottom: 60, left: 80 };
      const chartWidth = width - padding.left - padding.right;
      const chartHeight = height - padding.top - padding.bottom;
      
      ctx.clearRect(0, 0, width, height);
      
      const valores = this.dadosGrafico.flatMap(d => [d.receita, d.despesa, d.saldo]);
      const maxValor = Math.max(...valores, 0);
      const minValor = Math.min(...valores, 0);
      const range = maxValor - minValor;
      const margin = range * 0.1;
      const yMax = maxValor + margin;
      const yMin = minValor - margin;
      
      const getX = (index) => padding.left + (index * chartWidth) / Math.max(this.dadosGrafico.length - 1, 1);
      const getY = (valor) => padding.top + ((yMax - valor) * chartHeight) / (yMax - yMin);
      
      ctx.strokeStyle = '#f3f4f6';
      ctx.lineWidth = 1;
      
      for (let i = 0; i <= 5; i++) {
        const y = padding.top + (i * chartHeight) / 5;
        ctx.beginPath();
        ctx.moveTo(padding.left, y);
        ctx.lineTo(padding.left + chartWidth, y);
        ctx.stroke();
      }
      
      this.dadosGrafico.forEach((_, index) => {
        const x = getX(index);
        ctx.beginPath();
        ctx.moveTo(x, padding.top);
        ctx.lineTo(x, padding.top + chartHeight);
        ctx.stroke();
      });
      
      if (yMin < 0 && yMax > 0) {
        const zeroY = getY(0);
        ctx.strokeStyle = '#6b7280';
        ctx.lineWidth = 2;
        ctx.beginPath();
        ctx.moveTo(padding.left, zeroY);
        ctx.lineTo(padding.left + chartWidth, zeroY);
        ctx.stroke();
      }
      
      const linhas = [
        { dados: 'receita', cor: '#059669', label: 'Receitas' },
        { dados: 'despesa', cor: '#dc2626', label: 'Despesas' },
        { dados: 'saldo', cor: '#3b82f6', label: 'Saldo' }
      ];
      
      linhas.forEach(linha => {
        ctx.strokeStyle = linha.cor;
        ctx.lineWidth = 3;
        ctx.beginPath();
        
        this.dadosGrafico.forEach((item, index) => {
          const x = getX(index);
          const y = getY(item[linha.dados]);
          
          if (index === 0) {
            ctx.moveTo(x, y);
          } else {
            ctx.lineTo(x, y);
          }
        });
        
        ctx.stroke();
        
        ctx.fillStyle = linha.cor;
        this.dadosGrafico.forEach((item, index) => {
          const x = getX(index);
          const y = getY(item[linha.dados]);
          
          ctx.beginPath();
          ctx.arc(x, y, 4, 0, 2 * Math.PI);
          ctx.fill();
        });
      });
      
      ctx.fillStyle = '#6b7280';
      ctx.font = '12px sans-serif';
      ctx.textAlign = 'right';
      
      for (let i = 0; i <= 5; i++) {
        const valor = yMax - (i * (yMax - yMin)) / 5;
        const y = padding.top + (i * chartHeight) / 5;
        ctx.fillText(this.formatarBRL(valor), padding.left - 10, y + 4);
      }
      
      ctx.textAlign = 'center';
      this.dadosGrafico.forEach((item, index) => {
        const x = getX(index);
        ctx.fillText(item.mesFormatado, x, height - padding.bottom + 20);
      });
    },

    selecionarPeriodo(periodo) {
      this.periodoSelecionado = periodo;
      this.mostrarPersonalizado = false;
      this.buscarDados();
    },

    aplicarPeriodoPersonalizado() {
      if (!this.dataInicio || !this.dataFim) {
        alert('Por favor, selecione ambas as datas');
        return;
      }
      
      if (new Date(this.dataInicio) > new Date(this.dataFim)) {
        alert('Data de início deve ser anterior à data fim');
        return;
      }
      
      this.buscarDados();
    },

    formatarBRL(valor) {
      return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
      }).format(valor || 0);
    },

    voltarDashboard() {
      this.$router.push('/dashboard');
    },

    handleResize() {
      if (this.dadosGrafico.length > 0) {
        this.$nextTick(() => {
          this.criarGrafico();
        });
      }
    },
    ativarPersonalizado() {
      this.mostrarPersonalizado = !this.mostrarPersonalizado;
      if (this.mostrarPersonalizado) {
        this.periodoSelecionado = ''; 
      } else {
        this.periodoSelecionado = '12';
        this.buscarDados();
      }
    },
  },

  mounted() {
    const hoje = new Date();
    const umAnoAtras = new Date();
    umAnoAtras.setMonth(umAnoAtras.getMonth() - 12);

    this.dataFim = hoje.toISOString().split('T')[0];
    this.dataInicio = umAnoAtras.toISOString().split('T')[0];
    
    this.buscarDados();
    window.addEventListener('resize', this.handleResize);
  },

  beforeUnmount() {
    window.removeEventListener('resize', this.handleResize);
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
  align-items: center;
}

.dashboard-title {
  color: #1e293b;
  font-size: 20px;
  font-weight: 700;
  margin: 0;
  text-align: center;
}

.period-selector {
  display: flex;
  flex-wrap: wrap; /* permite quebrar linha */
  gap: 8px;
  background: rgba(255, 255, 255, 0.95);
  padding: 4px;
  border-radius: 8px;
  border: 1px solid rgba(226, 232, 240, 0.8);
  justify-content: center; /* centraliza os botões mesmo quebrando linha */
}

.period-btn {
  padding: 8px 12px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 6px;
  border: 1px solid #e2e8f0;
  background: white;
  color: #64748b;
  cursor: pointer;
  transition: all 0.2s ease;
  flex: 1 1 auto;  /* permite ajuste automático da largura */
  min-width: 100px;
  text-align: center;
}
.period-btn:hover {
  background: #f1f5f9;
  color: #374151;
}

.period-btn.active {
  background: #059669;
  color: white;
  border-color: #059669;
}

.resumo-cards {
  display: grid;
  grid-template-columns: 1fr;
  gap: 16px;
  margin-bottom: 24px;
}

.resumo-card {
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  backdrop-filter: blur(10px);
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
  flex-shrink: 0;
}

.resumo-card.receita .card-icon {
  background: rgba(5, 150, 105, 0.1);
  color: #059669;
}

.resumo-card.despesa .card-icon {
  background: rgba(220, 38, 38, 0.1);
  color: #dc2626;
}

.resumo-card.saldo .card-icon {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
}

.resumo-card.saldo.positivo .card-icon {
  background: rgba(5, 150, 105, 0.1);
  color: #059669;
}

.resumo-card.saldo.negativo .card-icon {
  background: rgba(220, 38, 38, 0.1);
  color: #dc2626;
}

.card-content {
  flex: 1;
}

.card-content h3 {
  color: #6b7280;
  font-size: 14px;
  font-weight: 500;
  margin: 0 0 4px 0;
}

.card-content .valor {
  color: #1f2937;
  font-size: 24px;
  font-weight: 700;
  margin: 0 0 4px 0;
}

.resumo-card.receita .valor {
  color: #059669;
}

.resumo-card.despesa .valor {
  color: #dc2626;
}

.resumo-card.saldo.positivo .valor {
  color: #059669;
}

.resumo-card.saldo.negativo .valor {
  color: #dc2626;
}

.periodo-info {
  color: #9ca3af;
  font-size: 12px;
}

.grafico-card, .dados-card {
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  backdrop-filter: blur(10px);
}

.grafico-header, .dados-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.grafico-header h3, .dados-header h3 {
  color: #374151;
  font-size: 18px;
  font-weight: 600;
  margin: 0;
}

.legenda {
  display: flex;
  gap: 20px;
}

.legenda-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #6b7280;
}

.cor-indicador {
  width: 12px;
  height: 12px;
  border-radius: 2px;
}

.legenda-item.receita .cor-indicador {
  background: #059669;
}

.legenda-item.despesa .cor-indicador {
  background: #dc2626;
}

.legenda-item.saldo .cor-indicador {
  background: #3b82f6;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: #6b7280;
}

.loading-spinner {
  width: 32px;
  height: 32px;
  border: 3px solid #f3f4f6;
  border-top: 3px solid #059669;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.empty-state {
  text-align: center;
  padding: 60px 24px;
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

.grafico-container {
  width: 100%;
  height: 400px;
  position: relative;
}

.grafico-canvas {
  width: 100%;
  height: 100%;
  border-radius: 8px;
}

.dados-table {
  border-radius: 8px;
  overflow: hidden;
}

.table-header {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr 1fr;
  gap: 16px;
  padding: 16px;
  background: #f8fafc;
  border-bottom: 1px solid #e5e7eb;
  font-weight: 600;
  color: #374151;
  font-size: 14px;
}

.table-body {
  display: flex;
  flex-direction: column;
}

.table-row {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr 1fr;
  gap: 16px;
  padding: 16px;
  border-bottom: 1px solid #f3f4f6;
  align-items: center;
  background: white;
  transition: all 0.2s ease;
}

.table-row:hover {
  background: #f9fafb;
}

.td {
  font-size: 14px;
  color: #374151;
}

.td.receita {
  color: #059669;
  font-weight: 600;
}

.td.despesa {
  color: #dc2626;
  font-weight: 600;
}

.td.saldo.positivo {
  color: #059669;
  font-weight: 600;
}

.td.saldo.negativo {
  color: #dc2626;
  font-weight: 600;
}

.td.variacao.positivo {
  color: #059669;
  font-weight: 500;
}

.td.variacao.negativo {
  color: #dc2626;
  font-weight: 500;
}

.td.variacao small {
  display: block;
  font-size: 12px;
  opacity: 0.8;
  margin-top: 2px;
}

.sem-variacao {
  color: #9ca3af;
}

.erro-alert {
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  gap: 8px;
  position: relative;
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.2);
  color: #dc2626;
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

  .resumo-cards {
    grid-template-columns: repeat(3, 1fr);
  }

  .legenda {
    gap: 24px;
  }
}

@media (max-width: 767px) {
  .table-header {
    display: none;
  }

  .table-row {
    display: block;
    padding: 16px;
    margin-bottom: 12px;
    border-radius: 8px;
    border: 1px solid #e5e7eb;
  }

  .td {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 8px 0;
    border-bottom: 1px solid #f3f4f6;
  }

  .td:last-child {
    border-bottom: none;
  }

  .td::before {
    content: attr(data-label);
    font-weight: 600;
    color: #6b7280;
    font-size: 12px;
    text-transform: uppercase;
  }

  .legenda {
    flex-direction: column;
    gap: 12px;
  }
}

.periodo-personalizado {
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 8px;
  padding: 16px;
  margin-top: 16px;
  width: 100%;
}

.data-inputs {
  display: flex;
  flex-direction: column;
  gap: 16px;
  align-items: stretch;
}

@media (min-width: 768px) {
  .data-inputs {
    display: grid;
    grid-template-columns: 1fr 1fr auto;
    gap: 16px;
    align-items: end;
  }
  
  .periodo-personalizado {
    max-width: 600px;
    margin: 16px auto 0;
  }
}

@media (min-width: 1024px) {
  .dashboard-header-section {
    align-items: flex-start;
  }
  
  .period-selector {
    flex-wrap: wrap;
    justify-content: center;
  }
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.input-group label {
  color: #374151;
  font-size: 14px;
  font-weight: 500;
}

.date-input {
  padding: 8px 12px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  background: white;
  color: #1f2937;
  font-size: 14px;
  transition: all 0.2s ease;
}

.date-input:focus {
  outline: none;
  border-color: #059669;
  box-shadow: 0 0 0 3px rgba(5, 150, 105, 0.1);
}

.btn-aplicar {
  padding: 8px 16px;
  background: #059669;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
  font-weight: 500;
  height: fit-content;
}

.btn-aplicar:hover {
  background: #047857;
}

.btn-debug {
  padding: 4px 8px;
  background: #f3f4f6;
  color: #6b7280;
  border: 1px solid #d1d5db;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.debug-info {
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 16px;
  font-size: 12px;
  color: #6b7280;
}

.debug-info p {
  margin: 4px 0;
}

@media (max-width: 767px) {
  .data-inputs {
    grid-template-columns: 1fr;
  }
}

</style>
