package sprint4;

import java.util.Random;
import java.util.Scanner;

public class Competicao {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        int numEquipes;
        boolean reiniciar = true; // Variável para controlar se o usuário deseja reiniciar
        do {
            boolean start = false;

            int[] equipes;
            char[][] resultados;
            int[] pontuacaoEquipes;
            int[] numCombatesEquipes; // Array para armazenar a quantidade de combates de cada equipe

            System.out.println("\n");
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("-------------------------------Bem - Vindo(a)-----------------------------");
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("--------------Ranking (2) das equipes participantes da RoboCup------------");
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------------");
            System.out.println();

            // 1ª Parte: Entrada da quantidade de equipes
            do {
                System.out.print("Digite a quantidade de equipes participantes da RoboCup: ");
                numEquipes = teclado.nextInt();

                if (numEquipes > 0) {
                    start = true;
                } else {
                    System.out.println("O número de equipes deve ser maior que 0. Informe um número válido para prosseguir.");
                    System.out.println("Reiniciando...");
                    System.out.println("\n");
                }
            } while (!start);

            // Inicialização dos arrays
            equipes = new int[numEquipes];
            numCombatesEquipes = new int[numEquipes];
            resultados = new char[numEquipes][];
            pontuacaoEquipes = new int[numEquipes];

            // Entrada do número de combates de cada equipe
            for (int i = 0; i < numEquipes; i++) {
                boolean numeroValido = false;
                do {
                    System.out.print("Número da " + (i + 1) + "ª equipe (de 11 a 99): ");
                    int numCadaEquipe = teclado.nextInt();

                    if (numCadaEquipe >= 11 && numCadaEquipe <= 99) {
                        boolean numeroJaUsado = false;

                        // Verifica se o número já foi usado
                        for (int j = 0; j < i; j++) {
                            if (equipes[j] == numCadaEquipe) {
                                numeroJaUsado = true;
                                break;
                            }
                        }

                        if (!numeroJaUsado) {
                            equipes[i] = numCadaEquipe;
                            numeroValido = true;
                        } else {
                            System.out.println("Número já em uso. Informe um número válido e único.");
                        }
                    } else {
                        System.out.println("Número inválido. Informe um número válido (de 11 a 99).");
                    }
                } while (!numeroValido);
            }
            
            System.out.println("\nFase Normal :");
            // Gerar combates "todos contra todos"
            int numeroCombate = 0; // Inicializa o número do combate
            for (int i = 0; i < numEquipes; i++) {
                for (int j = i + 1; j < numEquipes; j++) {
                    numeroCombate++; // Incrementa o número do combate
                    System.out.println("\n------" + numeroCombate + "°" + "Combate-------");
                    System.out.println("Combate entre equipe " + equipes[i] + " e equipe " + equipes[j]);

                    // Gerar valores aleatórios de pontuação para as equipes
                    Random random = new Random();
                    int pontuacaoEquipe1 = random.nextInt(10) + 1; // Valor aleatório entre 1 e 10
                    int pontuacaoEquipe2 = random.nextInt(10) + 1; // Valor aleatório entre 1 e 10

                    System.out.println("\n------" + numeroCombate + "°" + "Pontuação-------");
                    System.out.println("Pontuação da equipe " + equipes[i] + ": " + pontuacaoEquipe1);
                    System.out.println("Pontuação da equipe " + equipes[j] + ": " + pontuacaoEquipe2);

                    System.out.println("\n------" + numeroCombate + "°" + "Vencedor-------");
                    // Verificar o vencedor com base nas pontuações e notas de design
                    if (pontuacaoEquipe1 > pontuacaoEquipe2) {
                        System.out.println("Equipe " + equipes[i] + " venceu o combate.");
                        pontuacaoEquipes[i] += 2; // Equipe 1 venceu e ganha 2 pontos
                    } else if (pontuacaoEquipe2 > pontuacaoEquipe1) {
                        System.out.println("Equipe " + equipes[j] + " venceu o combate.");
                        pontuacaoEquipes[j] += 2; // Equipe 2 venceu e ganha 2 pontos
                    } else {
                        // Empate
                        System.out.println("Empate! Nota de design decide.");
                        int notaDesignEquipe1 = random.nextInt(10) + 1; // Nota de design aleatória entre 1 e 10
                        int notaDesignEquipe2 = random.nextInt(10) + 1; // Nota de design aleatória entre 1 e 10

                        System.out.println("Nota de design da equipe " + equipes[i] + ": " + notaDesignEquipe1);
                        System.out.println("Nota de design da equipe " + equipes[j] + ": " + notaDesignEquipe2);

                        if (notaDesignEquipe1 > notaDesignEquipe2) {
                            System.out.println("Equipe " + equipes[i] + " venceu o combate devido à nota de design.");
                            pontuacaoEquipes[i] += 2; // Equipe 1 venceu devido à nota de design e ganha 2 pontos
                        } else if (notaDesignEquipe2 > notaDesignEquipe1) {
                            System.out.println("Equipe " + equipes[j] + " venceu o combate devido à nota de design.");
                            pontuacaoEquipes[j] += 2; // Equipe 2 venceu devido à nota de design e ganha 2 pontos
                        } else {
                            System.out.println("O combate terminou em empate, e não é possível determinar um vencedor.");
                            pontuacaoEquipes[i] += 1; // Empate, cada equipe ganha 1 ponto
                            pontuacaoEquipes[j] += 1;
                        }
                    }
                }
            }

            // Determinar as 3 melhores equipes para a fase final
            int[] melhoresEquipes = new int[3]; // Array para armazenar as 3 melhores equipes
            for (int i = 0; i < 3; i++) {
                int melhorPontuacao = -1; // Inicializa com um valor negativo para encontrar a maior pontuação
                int equipeMelhorPontuacao = -1; // Inicializa com um valor negativo

                for (int equipe = 0; equipe < numEquipes; equipe++) {
                    if (pontuacaoEquipes[equipe] > melhorPontuacao) {
                        melhorPontuacao = pontuacaoEquipes[equipe];
                        equipeMelhorPontuacao = equipe;
                    }
                }

                if (equipeMelhorPontuacao != -1) {
                    melhoresEquipes[i] = equipes[equipeMelhorPontuacao];
                    pontuacaoEquipes[equipeMelhorPontuacao] = -1; // Marca a equipe como já escolhida para evitar repetição
                }
            }
            
            
         // Fase Final
            System.out.println("\n-------------------Fase Final - TOP 3-------------------");

            int[] pontuacaoFaseFinal = new int[3]; // Array para armazenar a pontuação das equipes na fase final

            // Gerar combates entre as três melhores equipes
            for (int i = 0; i < 3; i++) {
                for (int j = i + 1; j < 3; j++) {
                    int equipe1 = melhoresEquipes[i];
                    int equipe2 = melhoresEquipes[j];

                    System.out.println("\n------Fase Final - Combate entre equipe " + equipe1 + " e equipe " + equipe2 + "------");

                    // Gerar valores aleatórios de pontuação para as equipes na fase final
                    Random random = new Random();
                    int pontuacaoEquipe1 = random.nextInt(10) + 1; // Valor aleatório entre 1 e 10
                    int pontuacaoEquipe2 = random.nextInt(10) + 1; // Valor aleatório entre 1 e 10

                    System.out.println("Pontuação da equipe " + equipe1 + ": " + pontuacaoEquipe1);
                    System.out.println("Pontuação da equipe " + equipe2 + ": " + pontuacaoEquipe2);

                    // Determinar o vencedor da fase final
                    if (pontuacaoEquipe1 > pontuacaoEquipe2) {
                        System.out.println("Equipe " + equipe1 + " venceu o combate na fase final.");
                        pontuacaoFaseFinal[i] += 2; // Equipe 1 venceu e ganha 2 pontos na fase final
                    } else if (pontuacaoEquipe2 > pontuacaoEquipe1) {
                        System.out.println("Equipe " + equipe2 + " venceu o combate na fase final.");
                        pontuacaoFaseFinal[j] += 2; // Equipe 2 venceu e ganha 2 pontos na fase final
                    } else {
                        // Empate na fase final
                        System.out.println("Empate! Nota de design decide na fase final.");
                        int notaDesignEquipe1 = random.nextInt(10) + 1; // Nota de design aleatória entre 1 e 10
                        int notaDesignEquipe2 = random.nextInt(10) + 1; // Nota de design aleatória entre 1 e 10

                        System.out.println("Nota de design da equipe " + equipe1 + ": " + notaDesignEquipe1);
                        System.out.println("Nota de design da equipe " + equipe2 + ": " + notaDesignEquipe2);

                        if (notaDesignEquipe1 > notaDesignEquipe2) {
                            System.out.println("Equipe " + equipe1 + " venceu o combate devido à nota de design na fase final.");
                            pontuacaoFaseFinal[i] += 2; // Equipe 1 venceu devido à nota de design e ganha 2 pontos na fase final
                        } else if (notaDesignEquipe2 > notaDesignEquipe1) {
                            System.out.println("Equipe " + equipe2 + " venceu o combate devido à nota de design na fase final.");
                            pontuacaoFaseFinal[j] += 2; // Equipe 2 venceu devido à nota de design e ganha 2 pontos na fase final
                        } else {
                            System.out.println("O combate na fase final terminou em empate, e não é possível determinar um vencedor.");
                            pontuacaoFaseFinal[i] += 1; // Empate, cada equipe ganha 1 ponto na fase final
                            pontuacaoFaseFinal[j] += 1;
                        }
                    }
                }
            }

            // Exibir os resultados da fase final
            System.out.println("\n--------Resultados da Fase Final das 3 melhores equipes--------");
            for (int i = 0; i < 3; i++) {
                System.out.println("Equipe " + melhoresEquipes[i] + " Pontuação na Fase Final: " + pontuacaoFaseFinal[i]);
            }

            // Determinar o campeão da fase final
            int equipeCampeaFaseFinal = -1;
            int maiorPontuacaoFaseFinal = -1;
            for (int i = 0; i < 3; i++) {
                if (pontuacaoFaseFinal[i] > maiorPontuacaoFaseFinal) {
                    maiorPontuacaoFaseFinal = pontuacaoFaseFinal[i];
                    equipeCampeaFaseFinal = melhoresEquipes[i];
                }
            }

            System.out.println("\n--------Resultado da Competição--------");
            System.out.println("Equipe " + equipeCampeaFaseFinal + " é o campeão da fase final!");

            // Pergunta ao usuário se deseja reiniciar
            System.out.println("\n");
            System.out.print("\nDeseja reiniciar o sistema? (S para Sim, N para Não): ");
            char reiniciarEscolha = teclado.next().charAt(0);
            if (reiniciarEscolha == 'N' || reiniciarEscolha == 'n') {
                reiniciar = false; // Define como falso para encerrar o loop
            }
           
            
        } while (reiniciar);
    }
}