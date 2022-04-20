package com.example.petstate.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.petstate.R
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.view.LineChartView

import lecho.lib.hellocharts.model.LineChartData

import lecho.lib.hellocharts.model.PointValue




class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val values: MutableList<PointValue> = ArrayList()
        values.add(PointValue(0F, 2F))
        values.add(PointValue(1F, 4F))
        values.add(PointValue(2F, 3F))
        values.add(PointValue(3F, 4F))

        //In most cased you can call data model methods in builder-pattern-like manner.

        //In most cased you can call data model methods in builder-pattern-like manner.
        val line: Line = Line(values).setColor(Color.BLUE).setCubic(true)
        val lines: MutableList<Line> = ArrayList<Line>()
        lines.add(line)

        val data = LineChartData()
        data.lines = lines
        val chart = LineChartView(applicationContext)
        chart.lineChartData = data
        chart.isInteractive = true
    }
}