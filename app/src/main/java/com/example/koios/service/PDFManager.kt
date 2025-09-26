package com.example.koios.service

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.koios.model.Book
import java.io.File
import java.io.FileOutputStream

class PDFManager {
    @SuppressLint("SuspiciousIndentation")
    fun ceatePDF(books: List<Book>)
    {
        //set file directory and file
        val fileDirName: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path +"/Koios"
        val fileName = "BookList.pdf"

        if(!File(fileDirName).exists())
            File(fileDirName).mkdir()

        if(File(fileDirName, fileName).exists())
            File(fileDirName, fileName).delete()

        val file = File(fileDirName, fileName)

        //sort the database
        val alphabetList = listOf<String>("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")
        val sortedBookLists = ArrayList<List<Book>>()
        val booksWithAuthor = books.filter {it.author.isNotEmpty()}.toList()
        for(letter in alphabetList)
        {
            if(!booksWithAuthor.any {  it.author.uppercase().trim()[0].toString() == letter })
                continue

            sortedBookLists += booksWithAuthor.filter{ it -> it.author.uppercase().trim()[0].toString() == letter }.sortedBy { it.author}.toList()
        }
        sortedBookLists += books.filter {it.author.isEmpty()}.toList()

        //create the pdf fundamental
        val pageWidth = 2480
        val pageHeight = 3508
        var currentHeight = 300F
        var pageNumber = 1
        val pdfDocument = PdfDocument()
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth,pageHeight,pageNumber).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas
        val titlePaint = Paint().apply { textSize = 160F; isUnderlineText = true }
        val subTitlePaint = Paint().apply { textSize = 100F }
        val textPaint = Paint().apply { textSize = 70F }
        val authorPaint = Paint().apply { textSize=60F;color= Color.DKGRAY }
        val legendPaint = Paint().apply { textSize=90F;color= Color.RED;isFakeBoldText=true }
        val legendX = 60F
        val bookX = legendX+140F
        val authorX = bookX +40F

        //title
        drawTextHorizontalCenter(canvas,titlePaint,"Bücherliste",currentHeight)
        currentHeight += titlePaint.textSize + 20F

        //subtitle
        if(books.count() > 1)
            drawTextHorizontalCenter(canvas,subTitlePaint,books.count().toString() + " Bücher",currentHeight)
        else
            drawTextHorizontalCenter(canvas,subTitlePaint,books.count().toString() + " Buch",currentHeight)
        currentHeight += subTitlePaint.textSize +20F

        //body
        for(booklist in sortedBookLists)
        {
            if (booklist.isEmpty())
                continue

            //legend
            if(booklist.first().author.isEmpty())
            {
                canvas.drawText("#",legendX,currentHeight,legendPaint)
            }
            else{
                canvas.drawText(booklist[0].author[0].uppercase(),legendX,currentHeight,legendPaint)
            }
            currentHeight += legendPaint.textSize

            //books
            for(book in booklist)
            {
                if(currentHeight >= 3200)
                {
                    pdfDocument.finishPage(page)
                    pageNumber += 1
                    pageInfo = PdfDocument.PageInfo.Builder(pageWidth,pageHeight,pageNumber).create()
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    currentHeight = 200F
                }

                canvas.drawText(book.title,bookX,currentHeight,textPaint)
                canvas.drawCircle(bookX-2*textPaint.textSize*0.2.toFloat(),currentHeight-textPaint.textSize*0.35.toFloat(),textPaint.textSize*0.2.toFloat(),authorPaint)
                if(book.author.isNotEmpty())
                {
                    currentHeight += textPaint.textSize*1.1.toFloat()
                    canvas.drawText("-> "+book.author,authorX,currentHeight,authorPaint)
                }

                currentHeight += textPaint.textSize*1.2.toFloat()
            }

            currentHeight += textPaint.textSize*1.3.toFloat()
        }
        pdfDocument.finishPage(page)

        //write the file
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()
    }
}

fun drawTextHorizontalCenter(canvas: Canvas, paint: Paint, text: String,y: Float) {
    val bounds: Rect = Rect()
    paint.getTextBounds(text, 0, text.length, bounds)
    val x: Int = (canvas.getWidth() / 2) - (bounds.width() / 2)
    canvas.drawText(text, x.toFloat(), y, paint)
}