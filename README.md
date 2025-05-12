# java-B11207030-Eason-B11207042-Tony
尚未完成功能:T轉、生成在非遊玩區域、開始和結束畫面、儲存方塊、暫停、道具(未定)

v1.1: 新增面板10×20網格線、消行機制

v1.2: 調整畫面大小、新增計分功能和隨分數提高下落速度

v1.2.1: 加入End game功能、方塊顏色、修正一些寫法

v1.3: 預覽下個方塊(測試出End game bug)

v1.3.1: 新增反轉功能和快速下落

v1.3.2: 新增背景音樂、消除音效、結束音效

v1.3.3: 新增消除動畫、遊玩區域下移(生成在區域上的功能暫有問題)


## Class
```mermaid
classDiagram
    class Tetris {
        - GamePanel panel
        + main(String[] args)
    }

    class GamePanel {
        - GameBoard board
        + paintComponent(Graphics g)
        + startGame()
    }

    class GameBoard {
        - Block currentBlock
        - Block nextBlock
        + spawnBlock()
        + moveBlockDown()
        + rotateBlock()
        + clearLines()
    }
    
    class SoundPlayer {
    + playSoundOnce()
    }

    class InputHandler {
        + keyPressed(KeyEvent e)
    }

    class Block {
        <<abstract>>
        + rotate()
        + moveLeft()
        + moveRight()
        + moveDown()
    }

    class IBlock
    class JBlock
    class LBlock
    class OBlock
    class SBlock
    class TBlock
    class ZBlock

    Tetris --> GamePanel
    GamePanel --> GameBoard
    GameBoard --> Block
    InputHandler --> GameBoard
    Block <|-- IBlock
    Block <|-- JBlock
    Block <|-- LBlock
    Block <|-- OBlock
    Block <|-- SBlock
    Block <|-- TBlock
    Block <|-- ZBlock

```









## 時序圖
```mermaid
sequenceDiagram
    participant User
    participant InputHandler
    participant GameBoard
    participant GamePanel

    User ->> InputHandler: 按下鍵盤
    InputHandler ->> GameBoard: 處理輸入
    GameBoard ->> GameBoard: 更新方塊位置
    GameBoard ->> GamePanel: 通知重新繪製
    GamePanel ->> GameBoard: 呼叫 paintComponent()

```


## 流程圖

```mermaid
flowchart TD
    A[開始遊戲] --> B[播放背景音樂]
    B[播放背景音樂] --> C[生成新方塊]
    C --> D[方塊下落]
    D --> E{是否碰到底部或其他方塊?}
    E -- 是 --> F[固定方塊]
    F --> G{是否有可消除的行?}
    G -- 是 --> H[消除行並撥放消除音效]
    H --> I[生成新方塊]
    G -- 否 --> I
    E -- 否 --> D
    I --> J{是否遊戲結束?}
    J -- 是 --> K[播放結束音效並結束遊戲]
    J -- 否 --> D

```
          
