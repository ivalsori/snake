package com.ivalsori.snake

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import java.lang.Float.min
import java.util.LinkedList
import kotlin.random.Random

enum class Direction {
    UP,
    LEFT,
    RIGHT,
    DOWN
}

class Coord(private val field: Field, private var _x: Int, private var _y: Int) {
    val x: Int
        get() = _x

    val y: Int
        get() = _y

    init {
        _x %= field.height
        _y %= field.width
    }

    constructor(coord: Coord): this(coord.field, coord._x, coord._y)

    fun move(dir: Direction) {
        when (dir) {
            Direction.DOWN -> _x = (_x + 1) % field.height
            Direction.UP -> _x = (_x + field.height - 1) % field.height
            Direction.RIGHT -> _y = (_y + 1) % field.width
            Direction.LEFT -> _y = (_y + field.width - 1) % field.width
        }
    }

    override operator fun equals(other: Any?): Boolean {
        if (other is Coord) return this.x == other.x && this.y == other.y
        return false
    }

}

class Field(val height: Int, val width: Int, val res: Resources) {
    private var snakeBody = LinkedList<Direction>()

    private var snakeHead = Coord(this, 0, 2)

    private var snakeTail = Coord(this, 0, 0)

    private val hasSnake = Array(height) { BooleanArray(width) { false } }

    private var food = Coord(this, 0, 0)

    private var bitmap: Bitmap

    enum class TexParts(val value: Int) {
        HEAD_UP(3),
        HEAD_RIGHT(4),
        HEAD_LEFT(8),
        HEAD_DOWN(9),
        TAIL_UP(13),
        TAIL_RIGHT(14),
        TAIL_LEFT(18),
        TAIL_DOWN(19),
        MID_HOR(1),
        MID_VER(7),
        COR_LEFT_UP(12),
        COR_LEFT_DOWN(2),
        COR_RIGHT_UP(5),
        COR_RIGHT_DOWN(0),
        FOOD(15)
    }
    private fun replaceFood(): Boolean {
        return if (snakeBody.size == width * height) {
            false
        } else {
            do {
                food = Coord(this, Random.nextInt(height), Random.nextInt(width))
            } while (hasSnake[food.x][food.y])
            true
        }
    }

    private val texRects =  ArrayList<Rect>()

    init {
        bitmap = BitmapFactory.decodeResource(res, R.drawable.snake_graphics)
        val texWidth = bitmap.width / 5
        val texHeight = bitmap.height / 4
        for (i in 0 until 4) {
            for (j in 0 until 5) {
                texRects.add(Rect(j * texWidth, i * texHeight, (j + 1) * texWidth, (i + 1) * texHeight))
            }
        }
        snakeBody.addAll(listOf(Direction.RIGHT, Direction.RIGHT, Direction.RIGHT))
        val cur = Coord(snakeTail)
        for (partDir in snakeBody) {
            hasSnake[cur.x][cur.y] = true
            cur.move(partDir)
        }
        replaceFood()
    }

    fun turn(dir: Direction) {
        if (snakeBody[snakeBody.size - 2].ordinal + dir.ordinal != Direction.DOWN.ordinal + Direction.UP.ordinal) {
            snakeBody.removeLast()
            snakeBody.addLast(dir)
        }
    }

    fun move(): Boolean {
        snakeHead.move(snakeBody.last)
        if (snakeHead != food) {
            hasSnake[snakeTail.x][snakeTail.y] = false
            snakeTail.move(snakeBody.first)
            snakeBody.removeFirst()
        }
        snakeBody.addLast(snakeBody.last)
        val success = if (hasSnake[snakeHead.x][snakeHead.y]) {
            false
        } else {
            hasSnake[snakeHead.x][snakeHead.y] = true
            true
        }
        if (snakeHead == food) {
            if (!replaceFood()) {
                return false
            }
        }
        return success
    }

    fun draw(canvas: Canvas?) {
        val squareSide = min(canvas!!.width.toFloat() / width, canvas.height.toFloat() / height).toInt()
        val cur = Coord(snakeTail)
        val p = Paint()
        p.color = res.getColor(R.color.black, null)
        canvas.drawRect(Rect(0, 0, squareSide * width, squareSide * height), p)
        canvas.clipRect(Rect(5, 5, squareSide * width - 5, squareSide * height - 5))
        p.color = res.getColor(R.color.yellow, null)
        canvas.drawRect(Rect(0, 0, squareSide * width, squareSide * height), p)
        var it = snakeBody.iterator()
        var partDir = it.next()
        var curRect = Rect(cur.y * squareSide, cur.x * squareSide, (cur.y + 1) * squareSide, (cur.x + 1) * squareSide)
        var curTex = when(partDir) {
            Direction.DOWN -> texRects[TexParts.TAIL_DOWN.value]
            Direction.LEFT -> texRects[TexParts.TAIL_LEFT.value]
            Direction.RIGHT -> texRects[TexParts.TAIL_RIGHT.value]
            Direction.UP -> texRects[TexParts.TAIL_UP.value]
        }
        canvas.drawBitmap(bitmap, curTex, curRect, Paint())
        cur.move(partDir)
        var prevDir = partDir
        partDir = it.next()
        while (it.hasNext()) {
            curRect = Rect(cur.y * squareSide, cur.x * squareSide, (cur.y + 1) * squareSide, (cur.x + 1) * squareSide)
            curTex = if (partDir == prevDir) {
                when(partDir) {
                    Direction.DOWN, Direction.UP -> texRects[TexParts.MID_VER.value]
                    Direction.LEFT, Direction.RIGHT -> texRects[TexParts.MID_HOR.value]
                }
            } else {
                when(partDir) {
                    Direction.DOWN, Direction.RIGHT -> when(prevDir) {
                        Direction.UP, Direction.LEFT -> texRects[TexParts.COR_RIGHT_DOWN.value]
                        Direction.DOWN -> texRects[TexParts.COR_RIGHT_UP.value]
                        Direction.RIGHT -> texRects[TexParts.COR_LEFT_DOWN.value]
                    }
                    Direction.UP, Direction.LEFT -> when(prevDir) {
                        Direction.DOWN, Direction.RIGHT -> texRects[TexParts.COR_LEFT_UP.value]
                        Direction.UP -> texRects[TexParts.COR_LEFT_DOWN.value]
                        Direction.LEFT -> texRects[TexParts.COR_RIGHT_UP.value]
                    }
                }
            }
            canvas.drawBitmap(bitmap, curTex, curRect, Paint())
            cur.move(partDir)

            prevDir = partDir
            partDir = it.next()
        }
        curRect = Rect(cur.y * squareSide, cur.x * squareSide, (cur.y + 1) * squareSide, (cur.x + 1) * squareSide)
        curTex = when(prevDir) {
            Direction.DOWN -> texRects[TexParts.HEAD_DOWN.value]
            Direction.UP -> texRects[TexParts.HEAD_UP.value]
            Direction.LEFT -> texRects[TexParts.HEAD_LEFT.value]
            Direction.RIGHT -> texRects[TexParts.HEAD_RIGHT.value]
        }
        canvas.drawBitmap(bitmap, curTex, curRect, Paint())

        curRect = Rect(food.y * squareSide, food.x * squareSide, (food.y + 1) * squareSide, (food.x + 1) * squareSide)
        canvas.drawBitmap(bitmap, texRects[TexParts.FOOD.value], curRect, Paint())
    }

    val score: Int
        get() = snakeBody.size - 3
}