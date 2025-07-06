<template>
  <div class="periodo-selector-custom">
    <div class="selector-tabs">
      <button 
        v-for="periodo in periodosPreDefinidos" 
        :key="periodo.value"
        @click="selecionarPeriodoPreDefinido(periodo.value)"
        :class="['period-btn', { active: tipoSelecionado === 'predefinido' && periodoSelecionado === periodo.value }]"
      >
        {{ periodo.label }}
      </button>
      <button 
        @click="tipoSelecionado = 'personalizado'"
        :class="['period-btn', { active: tipoSelecionado === 'personalizado' }]"
      >
        Personalizado
      </button>
    </div>

    <div v-if="tipoSelecionado === 'personalizado'" class="periodo-personalizado">
      <div class="data-inputs">
        <div class="input-group">
          <label>Data Início</label>
          <input 
            type="date" 
            v-model="dataInicio" 
            @change="aplicarPeriodoPersonalizado"
            class="date-input"
          />
        </div>
        <div class="input-group">
          <label>Data Fim</label>
          <input 
            type="date" 
            v-model="dataFim" 
            @change="aplicarPeriodoPersonalizado"
            class="date-input"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SeletorPeriodoCustomizado',
  props: {
    periodoAtual: {
      type: String,
      default: '12'
    }
  },
  data() {
    return {
      tipoSelecionado: 'predefinido',
      periodoSelecionado: '12',
      dataInicio: '',
      dataFim: '',
      periodosPreDefinidos: [
        { value: '3', label: '3 meses' },
        { value: '6', label: '6 meses' },
        { value: '12', label: '12 meses' },
        { value: '24', label: '24 meses' }
      ]
    }
  },
  methods: {
    selecionarPeriodoPreDefinido(periodo) {
      this.tipoSelecionado = 'predefinido';
      this.periodoSelecionado = periodo;
      
      const dataFim = new Date();
      const dataInicio = new Date();
      dataInicio.setMonth(dataInicio.getMonth() - parseInt(periodo));
      
      this.$emit('periodo-alterado', {
        tipo: 'predefinido',
        periodo: periodo,
        dataMin: dataInicio.toISOString().split('T')[0],
        dataMax: dataFim.toISOString().split('T')[0]
      });
    },

    aplicarPeriodoPersonalizado() {
      if (this.dataInicio && this.dataFim) {
        if (new Date(this.dataInicio) > new Date(this.dataFim)) {
          alert('Data de início deve ser anterior à data fim');
          return;
        }

        this.$emit('periodo-alterado', {
          tipo: 'personalizado',
          dataMin: this.dataInicio,
          dataMax: this.dataFim
        });
      }
    }
  },

  mounted() {
    this.selecionarPeriodoPreDefinido(this.periodoAtual);
    
    this.dataFim = new Date().toISOString().split('T')[0];
    
    const dataInicio = new Date();
    dataInicio.setMonth(dataInicio.getMonth() - 12);
    this.dataInicio = dataInicio.toISOString().split('T')[0];
  }
}
</script>

<style scoped>
.periodo-selector-custom {
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 12px;
  padding: 16px;
  backdrop-filter: blur(10px);
}

.selector-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.period-btn {
  padding: 8px 16px;
  background: transparent;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  color: #64748b;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
  font-weight: 500;
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

.periodo-personalizado {
  border-top: 1px solid #e2e8f0;
  padding-top: 16px;
}

.data-inputs {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
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

@media (max-width: 767px) {
  .data-inputs {
    grid-template-columns: 1fr;
  }
  
  .selector-tabs {
    justify-content: center;
  }
  
  .period-btn {
    flex: 1;
    min-width: 80px;
  }
}
</style>
