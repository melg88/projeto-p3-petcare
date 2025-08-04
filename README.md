# PetCare - Aplicativo de Gerenciamento de Gatos

Um aplicativo Android desenvolvido em Kotlin para gerenciar gatos, tutores, vacinas e remédios, utilizando Firebase como banco de dados.

## Funcionalidades

### 🐱 Gerenciamento de Gatos
- Cadastrar novos gatos
- Editar informações dos gatos
- Excluir gatos
- Visualizar lista de gatos
- Associar gatos a tutores

### 👤 Gerenciamento de Tutores
- Cadastrar novos tutores
- Editar informações dos tutores
- Excluir tutores
- Visualizar lista de tutores

### 💉 Gerenciamento de Vacinas
- Cadastrar vacinas aplicadas
- Editar informações das vacinas
- Excluir registros de vacinas
- Visualizar histórico de vacinas por gato

### 💊 Gerenciamento de Remédios
- Cadastrar tratamentos com remédios
- Editar informações dos remédios
- Excluir registros de remédios
- Visualizar histórico de remédios por gato

## Tecnologias Utilizadas

- **Kotlin** - Linguagem de programação
- **Android SDK** - Framework Android
- **Firebase Firestore** - Banco de dados em tempo real
- **Material Design 3** - Design system
- **MVVM Architecture** - Padrão de arquitetura
- **LiveData** - Observação de dados
- **ViewBinding** - Binding de views
- **Navigation Component** - Navegação entre telas
- **RecyclerView** - Listas eficientes
- **Coroutines** - Programação assíncrona

## Configuração do Projeto

### Pré-requisitos

1. **Android Studio** (versão mais recente)
2. **JDK 8** ou superior
3. **Conta Google** para Firebase

### Configuração do Firebase

1. Acesse o [Firebase Console](https://console.firebase.google.com/)
2. Crie um novo projeto ou use um existente
3. Adicione um aplicativo Android:
   - Package name: `com.example.petcare`
   - Baixe o arquivo `google-services.json`
4. Coloque o arquivo `google-services.json` na pasta `app/` do projeto
5. Ative o Firestore Database no console do Firebase
6. Configure as regras de segurança do Firestore:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true; // Para desenvolvimento - configure adequadamente para produção
    }
  }
}
```

### Estrutura do Banco de Dados

O aplicativo utiliza as seguintes coleções no Firestore:

#### Model: `gatos`
```json
{
  "id": "string",
  "nome": "string",
  "idade": "number",
  "raca": "string",
  "peso": "number",
  "tutorId": "string",
  "tutorNome": "string",
  "observacoes": "string",
  "dataCadastro": "timestamp",
  "fotoUrl": "string"
}
```

#### Model: `tutores`
```json
{
  "id": "string",
  "nome": "string",
  "telefone": "string",
  "email": "string",
  "endereco": "string",
  "dataCadastro": "timestamp"
}
```

#### Model: `vacinas`
```json
{
  "id": "string",
  "nome": "string",
  "dataVacina": "timestamp",
  "proximaVacina": "timestamp",
  "gatoId": "string",
  "gatoNome": "string",
  "observacoes": "string",
  "dataCadastro": "timestamp"
}
```

#### Model: `remedios`
```json
{
  "id": "string",
  "nome": "string",
  "dosagem": "string",
  "frequencia": "string",
  "dataInicio": "timestamp",
  "dataFim": "timestamp",
  "gatoId": "string",
  "gatoNome": "string",
  "observacoes": "string",
  "dataCadastro": "timestamp"
}
```

## Como Executar Localmente

1. Clone o repositório:
```bash
git clone <url-do-repositorio>
cd projeto-p3-petcare
```

2. Abra o projeto no Android Studio

3. Configure o Firebase (veja seção acima)

4. Sincronize o projeto (File > Sync Project with Gradle Files)

5. Execute o aplicativo em um dispositivo ou emulador

## Estrutura do Projeto

```
app/src/main/java/com/example/petcare/
├── model/                 # Classes de dados
│   ├── Gato.kt
│   ├── Tutor.kt
│   ├── Vacina.kt
│   └── Remedio.kt
├── repository/            # Camada de acesso a dados
│   └── FirebaseRepository.kt
├── viewmodel/            # ViewModels
│   ├── GatoViewModel.kt
│   ├── TutorViewModel.kt
│   ├── VacinaViewModel.kt
│   └── RemedioViewModel.kt
├── ui/                   # Interface do usuário
│   ├── gatos/           # Telas de gatos
│   ├── tutores/         # Telas de tutores
│   ├── vacinas/         # Telas de vacinas
│   └── remedios/        # Telas de remédios
└── MainActivity.kt       # Activity principal
```

## Funcionalidades por Tela

### Tela de Gatos
- Lista todos os pets cadastrados
- Botão FAB para adicionar novo pet
- Botões de editar e excluir em cada item
- Informações exibidas: nome, raça, tutor, idade e peso

### Tela de Tutores
- Lista todos os tutores cadastrados
- Botão FAB para adicionar novo tutor
- Botões de editar e excluir em cada item
- Informações exibidas: nome, telefone, email e endereço

### Tela de Vacinas
- Lista todas as vacinas aplicadas
- Botão FAB para adicionar nova vacina
- Botões de editar e excluir em cada item
- Informações exibidas: nome da vacina, data, gato e observações

### Tela de Remédios
- Lista todos os remédios cadastrados
- Botão FAB para adicionar novo remédio
- Botões de editar e excluir em cada item
- Informações exibidas: nome, dosagem, frequência, gato e período

## Melhorias Futuras

- [ ] Autenticação de usuários
- [ ] Upload de fotos dos gatos
- [ ] Notificações para vacinas pendentes
- [ ] Relatórios e estatísticas
- [ ] Backup e sincronização offline
- [ ] Temas claro/escuro
- [ ] Suporte a outros tipos de pets
- [ ] Integração com veterinários
- [ ] Histórico médico completo

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

Desenvolvido com ❤️ para cuidar dos nossos amigos felinos! 
