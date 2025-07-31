# Configuração do Firebase - PetCare App

Este guia irá ajudá-lo a configurar o Firebase para o aplicativo PetCare.

## Passo 1: Criar Projeto no Firebase

1. Acesse o [Firebase Console](https://console.firebase.google.com/)
2. Clique em "Criar um projeto"
3. Digite um nome para o projeto (ex: "PetCare App")
4. Aceite os termos e clique em "Continuar"
5. Desative o Google Analytics se não precisar (opcional)
6. Clique em "Criar projeto"

## Passo 2: Adicionar Aplicativo Android

1. No console do Firebase, clique no ícone do Android
2. Digite o package name: `com.example.petcare`
3. Digite um apelido para o app (ex: "PetCare Android")
4. Clique em "Registrar app"
5. Baixe o arquivo `google-services.json`
6. Coloque o arquivo na pasta `app/` do seu projeto Android

## Passo 3: Configurar Firestore Database

1. No menu lateral, clique em "Firestore Database"
2. Clique em "Criar banco de dados"
3. Escolha "Iniciar no modo de teste" (para desenvolvimento)
4. Escolha a localização mais próxima (ex: "us-central1")
5. Clique em "Concluir"

## Passo 4: Configurar Regras de Segurança

1. Na aba "Regras" do Firestore, substitua as regras por:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Permitir leitura e escrita para todas as coleções
    match /gatos/{document} {
      allow read, write: if true;
    }
    match /tutores/{document} {
      allow read, write: if true;
    }
    match /vacinas/{document} {
      allow read, write: if true;
    }
    match /remedios/{document} {
      allow read, write: if true;
    }
  }
}
```

2. Clique em "Publicar"

## Passo 5: Verificar Configuração

1. Abra o Android Studio
2. Sincronize o projeto (File > Sync Project with Gradle Files)
3. Verifique se não há erros de compilação
4. Execute o aplicativo

## Estrutura das Coleções

O aplicativo criará automaticamente as seguintes coleções no Firestore:

### Coleção: `gatos`
- Documentos com informações dos gatos
- Campos: nome, idade, raça, peso, tutorId, tutorNome, observacoes, dataCadastro, fotoUrl

### Coleção: `tutores`
- Documentos com informações dos tutores
- Campos: nome, telefone, email, endereco, dataCadastro

### Coleção: `vacinas`
- Documentos com registros de vacinas
- Campos: nome, dataVacina, proximaVacina, gatoId, gatoNome, observacoes, dataCadastro

### Coleção: `remedios`
- Documentos com registros de remédios
- Campos: nome, dosagem, frequencia, dataInicio, dataFim, gatoId, gatoNome, observacoes, dataCadastro

## Solução de Problemas

### Erro: "google-services.json not found"
- Verifique se o arquivo está na pasta `app/`
- Sincronize o projeto no Android Studio

### Erro: "Firebase not initialized"
- Verifique se o plugin do Google Services está no build.gradle
- Verifique se o arquivo google-services.json está correto

### Erro: "Permission denied"
- Verifique as regras de segurança do Firestore
- Certifique-se de que as regras permitem leitura e escrita

### Erro: "Network error"
- Verifique a conexão com a internet
- Verifique se o Firestore está ativo no console

## Configuração para Produção

Para produção, você deve:

1. **Configurar autenticação de usuários**
2. **Restringir as regras de segurança**:
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

3. **Configurar domínios autorizados** no console do Firebase
4. **Ativar monitoramento** e alertas
5. **Configurar backup** automático

## Recursos Adicionais

- [Documentação do Firebase](https://firebase.google.com/docs)
- [Guia do Firestore](https://firebase.google.com/docs/firestore)
- [Regras de Segurança](https://firebase.google.com/docs/firestore/security/get-started)
- [Android SDK](https://firebase.google.com/docs/android/setup)

---

**Nota**: Este arquivo `google-services.json` é apenas um exemplo. Você deve substituí-lo pelo arquivo real gerado pelo Firebase Console. 
