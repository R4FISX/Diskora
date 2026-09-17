# Diskora

**A modern local disk analyzer built with Java.**

Diskora é um aplicativo desktop local para Windows que apresenta informações básicas sobre as unidades de armazenamento do computador.

## Escopo da primeira versão

- Dashboard com resumo do sistema e armazenamento.
- Tela **Armazenamento** com unidades lógicas como `C:`, `D:` e `E:`.
- Espaço total, usado, livre e percentual de uso.
- Sistema de arquivos.
- Tentativa de identificação do modelo e do tipo do disco: HDD, SSD ou NVMe.
- Leitura de informações básicas do sistema.

As áreas **Analisar**, **Limpeza** e **Configurações** aparecem na navegação, mas ainda estão em modo de prévia. Nenhum arquivo é apagado, nenhuma configuração do Windows é alterada e não há banco de dados, servidor, login ou API externa.

## Requisitos

- Windows 10 ou superior.
- Java 21.

O projeto usa Gradle Wrapper, portanto não é necessário instalar o Gradle globalmente.

## Executar

No Windows PowerShell:

```powershell
.\\gradlew.bat run
```

Se o disco do sistema estiver sem espaço, o cache nativo do JavaFX pode ser direcionado para outra unidade:

```powershell
.\\gradlew.bat run -PjavafxCacheDir="D:\\DiskoraJavafxCache" -PjavaTempDir="D:\\DiskoraJavaTemp"
```

Para gerar apenas os artefatos de compilação:

```powershell
.\\gradlew.bat build
```

## Estrutura

```text
src/main/java/com/diskora/
├── controller/   # Coordena serviços e interações da UI
├── model/        # Modelos imutáveis de domínio
├── service/      # Leitura local de discos e sistema via OSHI
├── util/         # Formatação e utilitários de apresentação
└── view/         # Componentes visuais JavaFX
```

## Observações sobre a detecção

O espaço das unidades é obtido por sistemas de arquivos montados. O pareamento entre uma unidade lógica e um disco físico usa as partições reportadas pelo OSHI. Em alguns computadores, principalmente com controladoras RAID, máquinas virtuais ou drivers que ocultam metadados, o modelo ou o tipo pode aparecer como **Desconhecido**.

## Licença

Este projeto ainda não define uma licença de distribuição.
