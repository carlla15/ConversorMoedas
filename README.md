📱 Conversor de Moedas – Android (Jetpack Compose)

![Android](https://img.shields.io/badge/Android-App-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Android-4285F4?logo=android&logoColor=white)

Aplicativo Android moderno para conversão de moedas em tempo real, desenvolvido com boas práticas de arquitetura e foco em performance, usabilidade e acessibilidade.

📲 DOWNLOAD

https://drive.google.com/uc?export=download&id=10_UwbfxYbAmhqYp8lpfOYExPdFswvqOA

Instalação rápida:
1. 📥 Baixe o APK pelo link acima
2. ⚙️ Ative "Fontes desconhecidas" (Configurações → Segurança)
3. 📱  Abra o APK e clique em "Instalar"
4. 🎉 Pronto! O app estará na tela inicial

Detalhes:
- 📦 Tamanho: 18 KB
- 📅 Versão: 1.0

🚀 Funcionalidades

- Conversão de moedas em tempo real  
- Histórico persistente de conversões  
- Troca rápida entre moedas  
- Suporte a múltiplas moedas e criptomoedas  


🧱 Arquitetura

O projeto segue o padrão MVVM (Model–View–ViewModel):

- View: Interface com Jetpack Compose  
- ViewModel: Gerenciamento de estado com StateFlow  
- Model/Repository: Comunicação com API e banco local  



✅ Requisitos Técnicos Atendidos

| Requisito            | Status | Implementação                          |
|----------------------|--------|------------------------------------------|
| Jetpack Compose      | ✅     | UI declarativa moderna                   |
| Arquitetura MVVM     | ✅     | Separação entre View, ViewModel e Model  |
| StateFlow / Flow     | ✅     | Gerenciamento de estados reativos        |
| One-shot events      | ✅     | Channel para eventos únicos              |
| Listas Responsivas   | ✅     | LazyColumn adaptável                     |
| Navigation Component | ✅     | Navegação entre telas                    |
| Acessibilidade       | ✅     | contentDescription e alto contraste      |
| Coroutines           | ✅     | viewModelScope + suspend functions       |
| Retrofit             | ✅     | Consumo de API REST                      |
| Room Database        | ✅     | Persistência local                       |



⭐ Diferenciais do Projeto

- Tratamento de erros robusto com fallback automático  
- Funciona totalmente offline após o primeiro uso  
- UI responsiva para diferentes tamanhos de tela  
- Tema personalizado com foco em acessibilidade  



🛠 Tecnologias Utilizadas

| Tecnologia          | Versão     | Finalidade                  |
|---------------------|------------|------------------------------|
| Kotlin              | 1.9.0      | Linguagem principal          |
| Jetpack Compose     | 2023.10.01 | Interface moderna            |
| Retrofit            | 2.9.0      | Consumo de API               |
| Room                | 2.6.0      | Persistência local           |
| Coroutines          | 1.7.3      | Programação assíncrona       |
| ViewModel           | 2.7.0      | Gerenciamento de estado      |
| Navigation Compose  | 2.7.5      | Navegação                    |
| Material Design 3   | —          | UI moderna                   |
| OkHttp              | 4.12.0     | Cliente HTTP                 |



🌐 API Utilizada

AwesomeAPI  
Endpoint: https://economia.awesomeapi.com.br  
Taxas de câmbio em tempo real  
Formato JSON simples  
Suporte a moedas fiat e criptomoedas  



📁 Estrutura do Projeto

```plaintext
CurrencyConverterApp/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/com/seuapp/conversor/
│           │   ├── data/
│           │   │   ├── local/
│           │   │   │   ├── dao/
│           │   │   │   ├── entity/
│           │   │   │   └── database/
│           │   │   ├── remote/
│           │   │   │   └── api/
│           │   │   └── repository/
│           │   ├── domain/
│           │   │   └── model/
│           │   ├── ui/
│           │   │   ├── screens/
│           │   │   ├── components/
│           │   │   └── navigation/
│           │   └── viewmodel/
│           ├── res/
│           └── AndroidManifest.xml
│
├── build.gradle
└── settings.gradle
