<template>
  <div class="login-wrapper">
    <div class="background-pattern"></div>
    
    <div class="login-container">
      <div class="login-icon">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M12 2V6M12 18V22M4.93 4.93L7.76 7.76M16.24 16.24L19.07 19.07M2 12H6M18 12H22M4.93 19.07L7.76 16.24M16.24 7.76L19.07 4.93" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          <circle cx="12" cy="12" r="3" stroke="currentColor" stroke-width="2"/>
        </svg>
      </div>
      <h2>Controle Financeiro</h2>
      <p class="subtitle">Acesse sua conta para gerenciar suas finanças</p>

      <form @submit.prevent="fazerLogin" class="login-form">
        <div class="input-group">
          <label for="email">E-mail</label>
          <div class="input-wrapper">
            <svg class="input-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M4 4H20C21.1 4 22 4.9 22 6V18C22 19.1 21.1 20 20 20H4C2.9 20 2 19.1 2 18V6C2 4.9 2.9 4 4 4Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <polyline points="22,6 12,13 2,6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <input 
              id="email"
              v-model="email" 
              type="email" 
              placeholder="seu@email.com" 
              required 
            />
          </div>
        </div>

        <div class="input-group">
          <label for="senha">Senha</label>
          <div class="input-wrapper">
            <svg class="input-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect x="3" y="11" width="18" height="11" rx="2" ry="2" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <circle cx="12" cy="16" r="1" fill="currentColor"/>
              <path d="M7 11V7C7 5.67392 7.52678 4.40215 8.46447 3.46447C9.40215 2.52678 10.6739 2 12 2C13.3261 2 14.5979 2.52678 15.5355 3.46447C16.4732 4.40215 17 5.67392 17 7V11" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <input 
              id="senha"
              v-model="senha" 
              :type="mostrarSenha ? 'text' : 'password'" 
              placeholder="••••••••" 
              required 
            />
            <button 
              type="button" 
              class="toggle-password" 
              @click="mostrarSenha = !mostrarSenha"
            >
              <svg v-if="mostrarSenha" width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M17.94 17.94C16.2306 19.243 14.1491 19.9649 12 20C5 20 1 12 1 12C2.24389 9.68192 4.231 7.81663 6.62 6.68M9.9 4.24C10.5883 4.0789 11.2931 3.99836 12 4C19 4 23 12 23 12C22.393 13.1356 21.6691 14.2048 20.84 15.19M14.12 14.12C13.8454 14.4148 13.5141 14.6512 13.1462 14.8151C12.7782 14.9791 12.3809 15.0673 11.9781 15.0744C11.5753 15.0815 11.1749 15.0074 10.8016 14.8565C10.4283 14.7056 10.0887 14.481 9.80385 14.1962C9.51900 13.9113 9.29439 13.5717 9.14351 13.1984C8.99262 12.8251 8.91853 12.4247 8.92563 12.0219C8.93274 11.6191 9.02091 11.2218 9.18488 10.8538C9.34884 10.4858 9.58525 10.1546 9.88 9.88" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <line x1="1" y1="1" x2="23" y2="23" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
              <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M1 12C1 12 5 4 12 4C19 4 23 12 23 12C23 12 19 20 12 20C5 20 1 12 1 12Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <circle cx="12" cy="12" r="3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </button>
          </div>
        </div>

        <div class="remember-forgot">
          <label class="remember-me">
            <input type="checkbox" v-model="lembrarMe">
            <span class="checkmark"></span>
            Lembrar-me
          </label>
          <a href="#" @click.prevent class="forgot-link">Esqueceu a senha?</a>
        </div>

        <button type="submit" class="login-button" :disabled="carregando">
          <span v-if="carregando" class="loading-spinner"></span>
          <span v-if="!carregando">
            Entrar
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M15 3H19C19.5304 3 20.0391 3.21071 20.4142 3.58579C20.7893 3.96086 21 4.46957 21 5V19C21 19.5304 20.7893 20.0391 20.4142 20.4142C20.0391 20.7893 19.5304 21 19 21H15M10 17L15 12L10 7M21 12H3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span v-else>Entrando...</span>
        </button>
      </form>

      <div v-if="erro" class="erro">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          <line x1="15" y1="9" x2="9" y2="15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          <line x1="9" y1="9" x2="15" y2="15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        {{ erro }}
      </div>

      <div class="register-link">
        <span>Não tem uma conta? </span>
        <a href="#" @click.prevent>Criar conta</a>
      </div>
    </div>
  </div>
</template>

<script>
import authService from '../services/authService'

export default {
  name: 'LoginView',
  data() {
    return {
      email: '',
      senha: '',
      erro: '',
      mostrarSenha: false,
      carregando: false,
      lembrarMe: false
    }
  },
  
  // Verificar se já está logado
  beforeMount() {
    if (authService.isAuthenticated()) {
      this.$router.push('/dashboard')
    }
  },
  
  methods: {
    async fazerLogin() {
      this.carregando = true
      this.erro = ''
      
      try {
        await authService.login(this.email, this.senha)
        
        // Sucesso no login
        console.log('Login realizado com sucesso!')
        this.$router.push('/dashboard')
        
      } catch (error) {
        console.error('Erro no login:', error)
        
        // Tratamento de diferentes tipos de erro
        if (error.message.includes('401') || error.message.includes('Credenciais')) {
          this.erro = 'E-mail ou senha inválidos'
        } else if (error.message.includes('fetch')) {
          this.erro = 'Erro de conexão. Verifique sua internet.'
        } else {
          this.erro = 'Erro interno. Tente novamente em alguns instantes.'
        }
      } finally {
        this.carregando = false
      }
    }
  }
}
</script>

<style scoped>
* {
  box-sizing: border-box;
}

.login-wrapper {
  min-height: 100vh;
  background: linear-gradient(135deg, #1e293b 0%, #334155 50%, #1e293b 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  position: relative;
}

.background-pattern {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(circle at 1px 1px, rgba(148, 163, 184, 0.1) 1px, transparent 0);
  background-size: 40px 40px;
  opacity: 0.3;
}

.login-container {
  width: 100%;
  max-width: 400px;
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 12px;
  padding: 32px;
  backdrop-filter: blur(10px);
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
  position: relative;
}

.login-icon {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #059669 0%, #10b981 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  color: white;
}

.login-container h2 {
  color: #1e293b;
  font-size: 24px;
  font-weight: 700;
  text-align: center;
  margin: 0 0 8px 0;
  letter-spacing: -0.025em;
}

.subtitle {
  color: #64748b;
  font-size: 14px;
  text-align: center;
  margin: 0 0 28px 0;
  line-height: 1.5;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
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

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 12px;
  color: #9ca3af;
  z-index: 1;
}

.input-wrapper input {
  width: 100%;
  padding: 12px 12px 12px 40px;
  background: white;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  color: #1f2937;
  font-size: 16px;
  transition: all 0.2s ease;
}

.input-wrapper input:focus {
  outline: none;
  border-color: #059669;
  box-shadow: 0 0 0 3px rgba(5, 150, 105, 0.1);
}

.input-wrapper input::placeholder {
  color: #9ca3af;
}

.toggle-password {
  position: absolute;
  right: 12px;
  background: none;
  border: none;
  color: #9ca3af;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  transition: color 0.2s ease;
}

.toggle-password:hover {
  color: #6b7280;
}

.remember-forgot {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: -8px;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #374151;
  cursor: pointer;
}

.remember-me input[type="checkbox"] {
  width: 16px;
  height: 16px;
  accent-color: #059669;
}

.forgot-link {
  color: #059669;
  font-size: 14px;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s ease;
}

.forgot-link:hover {
  color: #047857;
}

.login-button {
  width: 100%;
  padding: 12px 16px;
  background: linear-gradient(135deg, #059669 0%, #10b981 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 8px;
}

.login-button:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 10px 25px -5px rgba(5, 150, 105, 0.4);
}

.login-button:disabled {
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

.erro {
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.2);
  color: #dc2626;
  padding: 12px;
  border-radius: 8px;
  font-size: 14px;
  margin-top: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.register-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #6b7280;
}

.register-link a {
  color: #059669;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s ease;
}

.register-link a:hover {
  color: #047857;
}

@media (max-width: 480px) {
  .login-container {
    padding: 24px;
    margin: 16px;
  }
  
  .remember-forgot {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }
}
</style>
