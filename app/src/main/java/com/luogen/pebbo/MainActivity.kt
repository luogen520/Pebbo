package com.luogen.pebbo

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private lateinit var preview: TextView

    private var displayStr = "0"
    private var stored: Double? = null
    private var op = ""
    private var fresh = true

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_main)
        display = findViewById(R.id.display)
        preview = findViewById(R.id.preview)
        wireButtons()
        if (s != null) restore(s)
        paint()
    }

    private fun wireButtons() {
        val n = mapOf(
            R.id.b0 to "0", R.id.b1 to "1", R.id.b2 to "2",
            R.id.b3 to "3", R.id.b4 to "4", R.id.b5 to "5",
            R.id.b6 to "6", R.id.b7 to "7", R.id.b8 to "8", R.id.b9 to "9"
        )
        n.forEach { (id, d) -> findViewById<Button>(id).setOnClickListener { v -> pushDigit(d); v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY) } }
        findViewById<Button>(R.id.b_dot).setOnClickListener { v -> pushDot(); v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY) }
        findViewById<Button>(R.id.b_add).setOnClickListener { v -> pushOp("+"); v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY) }
        findViewById<Button>(R.id.b_sub).setOnClickListener { v -> pushOp("−"); v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY) }
        findViewById<Button>(R.id.b_mul).setOnClickListener { v -> pushOp("×"); v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY) }
        findViewById<Button>(R.id.b_div).setOnClickListener { v -> pushOp("÷"); v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY) }
        findViewById<Button>(R.id.b_eq).setOnClickListener { v -> doEq(); v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY) }
        findViewById<Button>(R.id.b_clr).setOnClickListener { v -> doClear(); v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY) }
        findViewById<Button>(R.id.b_del).setOnClickListener { v -> doDel(); v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY) }
        findViewById<Button>(R.id.b_neg).setOnClickListener { v -> flipSign(); v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY) }
        findViewById<Button>(R.id.b_pct).setOnClickListener { v -> percent(); v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY) }
    }

    private fun pushDigit(d: String) {
        if (fresh) {
            displayStr = d
            fresh = false
        } else if (displayStr == "0") {
            displayStr = d
        } else if (displayStr == "-0") {
            displayStr = "-" + d
        } else {
            if (displayStr.replace(".", "").replace("-", "").length >= 12) return
            displayStr += d
        }
        paint()
    }

    private fun pushDot() {
        if (fresh) {
            displayStr = "0."
            fresh = false
        } else if (!displayStr.contains(".")) {
            displayStr += "."
        }
        paint()
    }

    private fun pushOp(o: String) {
        if (stored != null && !fresh) {
            calc()
        }
        if (displayStr.isNotEmpty() && displayStr != "-" && displayStr != "Error") {
            stored = parseNum(displayStr)
        }
        op = o
        fresh = true
        paint()
    }

    private fun doEq() {
        if (op.isEmpty() || stored == null) return
        calc()
        op = ""
        stored = null
        fresh = true
        paint()
    }

    private fun calc() {
        val a = stored ?: return
        val b = parseNum(displayStr) ?: return
        val r = when (op) {
            "+" -> a + b
            "−" -> a - b
            "×" -> a * b
            "÷" -> if (b == 0.0) null else a / b
            else -> null
        }
        if (r == null) {
            displayStr = "Error"
        } else {
            displayStr = fmt(r)
            stored = r
        }
    }

    private fun fmt(d: Double): String {
        if (d.isInfinite() || d.isNaN()) return "Error"
        val l = d.toLong()
        if (l.toDouble() == d && kotlin.math.abs(d) < 1e15) return l.toString()
        var s = "%.10f".format(d).trimEnd('0').trimEnd('.')
        if (s.isEmpty() || s == "-") s = "0"
        return s
    }

    private fun parseNum(s: String): Double? {
        if (s == "Error" || s.isEmpty() || s == "-") return null
        return s.toDoubleOrNull()
    }

    private fun doClear() {
        displayStr = "0"
        stored = null
        op = ""
        fresh = true
        paint()
    }

    private fun doDel() {
        if (fresh) return
        if (displayStr.length <= 1 || (displayStr.length == 2 && displayStr.startsWith("-"))) {
            displayStr = "0"
            fresh = true
        } else {
            displayStr = displayStr.dropLast(1)
        }
        paint()
    }

    private fun flipSign() {
        if (displayStr == "0" || displayStr == "Error") return
        displayStr = if (displayStr.startsWith("-")) displayStr.substring(1) else "-" + displayStr
        paint()
    }

    private fun percent() {
        if (displayStr == "Error") return
        val d = parseNum(displayStr) ?: return
        displayStr = fmt(d / 100.0)
        fresh = true
        paint()
    }

    private fun paint() {
        display.text = displayStr
        val p = if (stored != null && op.isNotEmpty()) {
            fmt(stored!!) + " " + op
        } else ""
        preview.text = p
    }

    private fun restore(s: Bundle) {
        displayStr = s.getString("d", "0")
        op = s.getString("o", "")
        stored = if (s.containsKey("s")) s.getDouble("s") else null
        fresh = s.getBoolean("f", true)
    }

    override fun onSaveInstanceState(out: Bundle) {
        super.onSaveInstanceState(out)
        out.putString("d", displayStr)
        out.putString("o", op)
        if (stored != null) out.putDouble("s", stored!!)
        out.putBoolean("f", fresh)
    }
}
