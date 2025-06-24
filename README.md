📱 AvaliaDF - Guia e Avaliação de Estabelecimentos

O AvaliaDF é um aplicativo Android nativo desenvolvido como protótipo funcional para a disciplina de Projeto Integrador. Ele funciona como um guia colaborativo de estabelecimentos do Distrito Federal, permitindo que usuários descubram, avaliem e cadastrem novos locais, fomentando uma comunidade de "caçadores de experiências".

O projeto foi construído utilizando as tecnologias e padrões mais modernos de desenvolvimento Android, com foco em uma arquitetura limpa, escalável e conectada a serviços de backend na nuvem.

✨ Funcionalidades Principais
O aplicativo conta com um fluxo de usuário completo, incluindo:

👤 Autenticação e Perfil
[x] Cadastro de Usuário: Criação de conta com nome, e-mail e senha.
[x] Login Seguro: Acesso à plataforma com credenciais existentes.
[x] Recuperação de Senha: Sistema de redefinição de senha via e-mail.
[x] Logout Seguro: Encerra a sessão do usuário.
[x] Perfil de Usuário: Tela para visualizar dados como nome, e-mail e a quantidade de AvaliaCoins.
[x] Edição de Perfil: Permite que o usuário atualize informações como nome e CPF.
[x] Alteração de Senha: Funcionalidade para o usuário logado alterar sua senha atual.
📍 Descoberta e Listagem
[x] Tela Principal Dinâmica: Exibe botões de acesso rápido para categorias e uma lista horizontal com as cidades em destaque.
[x] Filtragem por Categoria: Navegação para listas de estabelecimentos filtrados por "Restaurantes", "Lazer" e "Hotel".
[x] Filtragem por Cidade: Ao clicar em uma cidade em destaque, exibe todos os estabelecimentos daquele local.
[x] Filtro Combinado: Dentro de uma lista de categoria, o usuário pode aplicar um segundo filtro por cidade através de um menu Spinner.
⭐️ Avaliação e Interação
[x] Tela de Detalhes Completa: Mostra todas as informações de um estabelecimento, incluindo nome, foto, descrição, endereço, horário e telefone.
[x] Cálculo de Média em Tempo Real: A nota exibida no card da lista é a média real de todas as avaliações, atualizada automaticamente.
[x] Lista de Comentários: Exibe as avaliações e comentários que outros usuários deixaram.
[x] Formulário de Avaliação Multi-etapas: Uma interface inteligente que guia o usuário por 4 etapas para avaliar um local.
[x] Sistema de Recompensa (AvaliaCoins): Usuários ganham 50 moedas virtuais a cada avaliação enviada com sucesso.
📝 Criação de Conteúdo
[x] Cadastro de Estabelecimentos: Usuários logados podem adicionar novos locais ao banco de dados através de um formulário completo no aplicativo.
🛠️ Arquitetura e Tecnologias Utilizadas
O projeto foi construído seguindo as melhores práticas recomendadas pelo Google, utilizando:

Linguagem: Kotlin
Arquitetura: MVVM (Model-View-ViewModel)
Componentes de Arquitetura Android (Jetpack):
View Binding: Para conectar layouts aos componentes de UI de forma segura.
LiveData: Para criar fluxos de dados observáveis que notificam a UI sobre mudanças.
ViewModel: Para separar a lógica de negócio da UI.
Navigation Component: Para gerenciar a navegação entre as telas (fragmentos) de forma visual e segura.
RecyclerView: Para exibir listas de cidades e estabelecimentos de forma eficiente.
SplashScreen API: Para criar a tela de abertura nativa do Android 12+.
Backend (Firebase):
Firebase Authentication: Para gerenciar o ciclo completo de autenticação de usuários.
Cloud Firestore: Como nosso banco de dados NoSQL em tempo real para armazenar usuários, cidades, estabelecimentos e avaliações.
Transações do Firestore: Para garantir a atualização atômica e segura da nota média dos estabelecimentos.
Bibliotecas de Terceiros:
Glide: Para carregar, armazenar em cache e exibir imagens de forma eficiente.
Coroutines: Para gerenciar operações assíncronas de forma moderna e legível.
🚀 Como Executar o Projeto
Para rodar este projeto localmente, siga os passos abaixo:

1. Clone o Repositório:

Bash

git clone https://github.com/Brun0495/AvaliaDF--CEUB.git
2. Configure o Firebase:

Vá para o console do Firebase e crie um novo projeto.
Adicione um novo aplicativo Android ao seu projeto Firebase, usando com.example.avaliadf como o nome do pacote.
Siga as instruções para baixar o arquivo google-services.json e coloque-o na pasta app/ do seu projeto no Android Studio.
No console, ative os seguintes serviços:
Authentication: Habilite o provedor "Email/Password".
Firestore Database: Crie um novo banco de dados no modo de teste.
Crie as coleções no Firestore com os mesmos nomes que usamos: users, citties, establishments, e reviews. Adicione alguns documentos de exemplo para testar.
3. Compile e Execute:

Abra o projeto no Android Studio.
Deixe o Gradle sincronizar as dependências.
Clique em Build > Rebuild Project para garantir que tudo está limpo.
Execute o aplicativo em um emulador ou dispositivo físico.
