(function () {
  // Obter o elemento de tela de desenho (canvas) e seu contexto 2D
  const cnv = document.querySelector('#canvas');
  const ctx = cnv.getContext('2d');

  // Definir a classe Quadrado para representar objetos no jogo
  class Quadrado {
    constructor(posX, posY, width, height, image, color, velocidade) {
      this.posX = posX;
      this.posY = posY;
      this.width = width;
      this.height = height;
      this.image = image;
      this.color = color;
      this.velocidade = velocidade;
    }
  }

  // Variáveis de controle de movimento do jogador 1
  let moveLeft = false;
  let moveUp = false;
  let moveRight = false;
  let moveDown = false;

  // Variáveis de controle de movimento do jogador 2
  let moveW = false;
  let moveS = false;
  let moveA = false;
  let moveD = false;

  // Contador de colisões
  let colisoes = 0;

  // Array para armazenar os quadrados (objetos) no jogo
  const quadrados = [];

  // Carregar a imagem do jogador 1
  const jogadorImg = new Image();
  jogadorImg.src = './imgs/robo-trasnaparente2.png';
  jogadorImg.onload = function () {
    // Criar o jogador 1
    const quadrado1 = new Quadrado(10, 220, 90, 95, jogadorImg, null, 5);
    quadrados.push(quadrado1);

    // Carregar a imagem do jogador 2
    const robôImg = new Image();
    robôImg.src = './imgs/robo-transparente1.png';
    robôImg.onload = function () {
      // Criar o jogador 2
      const quadrado2 = new Quadrado(cnv.width - 60, 230, 90, 95, robôImg, null, 5);
      quadrados.push(quadrado2);

      // Criar obstáculos
      const obstaculo1 = new Quadrado(10, 20, 550, 30, null, 'red', 0); 
      quadrados.push(obstaculo1);

      const obstaculo2 = new Quadrado(450, 450, 500, 30, null, 'red', 0); 
      quadrados.push(obstaculo2);

      // Variáveis de vida dos jogadores
      let jogadorVida = 100;
      let robôVida = 100;

      // Elementos HTML para exibir a vida dos jogadores e o vencedor
      const vidaRobo1Element = document.getElementById('vidaRobo1');
      const vidaRobo2Element = document.getElementById('vidaRobo2');
      const vencedorElement = document.getElementById('vencedor');
      const colisaoElement = document.getElementById('colisao');

      // Funções para atualizar a vida dos jogadores e exibir o vencedor
      function atualizarVidaRobo1() {
        vidaRobo1Element.textContent = `Vida do E-CHO: ${jogadorVida > 0 ? jogadorVida : 0}%`;
      }

      function atualizarVidaRobo2() {
        vidaRobo2Element.textContent = `Vida do BMAX: ${robôVida > 0 ? robôVida : 0}%`;
      }

      function atualizarVencedor() {
        if (colisoes >= 5 || jogadorVida <= 0 || robôVida <= 0) {
          let vencedor = '';
          if (jogadorVida > robôVida) {
            vencedor = 'E-CHO';
          } else if (robôVida > jogadorVida) {
            vencedor = 'BMAX';
          } else {
            vencedor = 'Empate';
          }
          vencedorElement.textContent = `Vencedor: ${vencedor}`;
          // Impedir movimento quando um dos robôs perde
          moveLeft = false;
          moveUp = false;
          moveRight = false;
          moveDown = false;
          moveW = false;
          moveS = false;
          moveA = false;
          moveD = false;
        }
        if (colisoes >= 5) {
          // Remover os robôs da lista para que eles desapareçam
          quadrados.splice(0, quadrados.length);
        }
      }

      // Inicializar as informações de vida dos jogadores
      atualizarVidaRobo1();
      atualizarVidaRobo2();

      // Adicionar event listeners para capturar teclas pressionadas
      window.addEventListener('keydown', function (e) {
        const nomeKey = e.key;
        switch (nomeKey) {
          case 'ArrowLeft':
            moveLeft = true;
            break;
          case 'ArrowUp':
            moveUp = true;
            break;
          case 'ArrowRight':
            moveRight = true;
            break;
          case 'ArrowDown':
            moveDown = true;
            break;
          case 'w':
            moveW = true;
            break;
          case 's':
            moveS = true;
            break;
          case 'a':
            moveA = true;
            break;
          case 'd':
            moveD = true;
            break;
        }
      });

      // Adicionar event listener para capturar teclas soltas
      window.addEventListener('keyup', (e) => {
        const key = e.key;
        switch (key) {
          case 'ArrowLeft':
            moveLeft = false;
            break;
          case 'ArrowUp':
            moveUp = false;
            break;
          case 'ArrowRight':
            moveRight = false;
            break;
          case 'ArrowDown':
            moveDown = false;
            break;
          case 'w':
            moveW = false;
            break;
          case 's':
            moveS = false;
            break;
          case 'a':
            moveA = false;
            break;
          case 'd':
            moveD = false;
            break;
        }
      });

      // Função para verificar colisões entre objetos
      function checkCollision(objeto1, objeto2) {
        return (
          objeto1.posX < objeto2.posX + objeto2.width &&
          objeto1.posX + objeto1.width > objeto2.posX &&
          objeto1.posY < objeto2.posY + objeto2.height &&
          objeto1.posY + objeto1.height > objeto2.posY
        );
      }

      // Função para mover os quadrados (jogadores e obstáculos)
      function moverQuadrados() {
        // Movimento do jogador 1
        if (moveLeft && !moveRight) {
          quadrados[0].posX -= quadrados[0].velocidade;
        }
        if (moveRight && !moveLeft) {
          quadrados[0].posX += quadrados[0].velocidade;
        }
        if (moveUp && !moveDown) {
          quadrados[0].posY -= quadrados[0].velocidade;
        }
        if (moveDown && !moveUp) {
          quadrados[0].posY += quadrados[0].velocidade;
        }

        // Limitar a posição do jogador 1 dentro do canvas
        quadrados[0].posX = Math.max(0, Math.min(cnv.width - quadrados[0].width, quadrados[0].posX));
        quadrados[0].posY = Math.max(0, Math.min(cnv.height - quadrados[0].height, quadrados[0].posY));

        // Verificar colisões do jogador 1 com obstáculos
        for (let i = 1; i < quadrados.length; i++) {
          if (quadrados[i].color) {
            if (checkCollision(quadrados[0], quadrados[i])) {
              if (moveLeft) {
                quadrados[0].posX = quadrados[i].posX + quadrados[i].width;
              }
              if (moveRight) {
                quadrados[0].posX = quadrados[i].posX - quadrados[0].width;
              }
              if (moveUp) {
                quadrados[0].posY = quadrados[i].posY + quadrados[i].height;
              }
              if (moveDown) {
                quadrados[0].posY = quadrados[i].posY - quadrados[0].height;
              }
            }
          }
        }

        // Movimento do jogador 2
        if (moveA && !moveD) {
          quadrados[1].posX -= quadrados[1].velocidade;
        }
        if (moveD && !moveA) {
          quadrados[1].posX += quadrados[1].velocidade;
        }
        if (moveW && !moveS) {
          quadrados[1].posY -= quadrados[1].velocidade;
        }
        if (moveS && !moveW) {
          quadrados[1].posY += quadrados[1].velocidade;
        }

        // Limitar a posição do jogador 2 dentro do canvas
        quadrados[1].posX = Math.max(0, Math.min(cnv.width - quadrados[1].width, quadrados[1].posX));
        quadrados[1].posY = Math.max(0, Math.min(cnv.height - quadrados[1].height, quadrados[1].posY));

        // Verificar colisões do jogador 2 com obstáculos
        for (let i = 2; i < quadrados.length; i++) {
          if (quadrados[i].color) {
            if (checkCollision(quadrados[1], quadrados[i])) {
              if (moveA) {
                quadrados[1].posX = quadrados[i].posX + quadrados[i].width;
              }
              if (moveD) {
                quadrados[1].posX = quadrados[i].posX - quadrados[1].width;
              }
              if (moveW) {
                quadrados[1].posY = quadrados[i].posY + quadrados[i].height;
              }
              if (moveS) {
                quadrados[1].posY = quadrados[i].posY - quadrados[1].height;
              }
            }
          }
        }

        // Verificar colisão entre os jogadores
        if (colisoes < 5 && checkCollision(quadrados[0], quadrados[1])) {
          if (colisoes === 0) {
            quadrados[0].posX = 10;
            quadrados[0].posY = 220;
            quadrados[1].posX = cnv.width - 60;
            quadrados[1].posY = 230;
          }
          colisoes++;
          if (colisoes >= 2) {
            quadrados[0].posX = 10;
            quadrados[0].posY = 220;
            quadrados[1].posX = cnv.width - 60;
            quadrados[1].posY = 230;
          }

          // Calcular o dano aleatório causado pelos jogadores
          const jogadorDano = Math.floor(Math.random() * 21);
          const robôDano = Math.floor(Math.random() * 21);

          // Atualizar a vida dos jogadores
          jogadorVida -= jogadorDano;
          robôVida -= robôDano;

          // Atualizar as informações de vida e verificar o vencedor
          atualizarVidaRobo1();
          atualizarVidaRobo2();
          atualizarVencedor();
        }

        // Atualizar o contador de colisões
        colisaoElement.textContent = `${colisoes}`;
      }

      // Função para exibir os quadrados no canvas
      function exibirQuadrados() {
        // Limpar o canvas
        ctx.clearRect(0, 0, cnv.width, cnv.height);
        // Desenhar os quadrados (objetos) no canvas
        for (const i in quadrados) {
          const spr = quadrados[i];
          if (spr.image) {
            // Se o objeto tem uma imagem, desenhe a imagem
            ctx.drawImage(spr.image, spr.posX, spr.posY, spr.width, spr.height);
          } else if (spr.color) {
            // Se o objeto tem uma cor, desenhe um retângulo preenchido
            ctx.fillStyle = spr.color;
            ctx.fillRect(spr.posX, spr.posY, spr.width, spr.height);
          }
        }
      }

      // Função para atualizar a tela do jogo usando animação
      function atualizarTela() {
        window.requestAnimationFrame(atualizarTela, cnv);
        moverQuadrados();
        exibirQuadrados();
      }

      // Iniciar a atualização da tela
      atualizarTela();
    };
  };
})();