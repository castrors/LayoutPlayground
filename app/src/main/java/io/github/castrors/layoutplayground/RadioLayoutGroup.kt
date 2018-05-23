package io.github.castrors.layoutplayground

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.LinearLayout

class RadioLayoutGroup : LinearLayout {

    var checkedRadioLayoutId = -1
        private set
    private var mChildOnCheckedChangeListener: CompoundLayout.OnCheckedChangeListener? = null
    private var mProtectFromCheckedChange = false
    private var mOnCheckedChangeListener: OnCheckedChangeListener? = null
    private var mPassThroughListener: PassThroughHierarchyChangeListener? = null

    constructor(context: Context) : super(context) {
        orientation = LinearLayout.VERTICAL
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val attributes = context.obtainStyledAttributes(
                attrs, R.styleable.RadioLayoutGroup, R.attr.radioButtonStyle, 0)

        val value = attributes.getResourceId(R.styleable.RadioLayoutGroup_checkedButton, View.NO_ID)
        if (value != View.NO_ID) {
            checkedRadioLayoutId = value
        }

        val index = attributes.getInt(R.styleable.RadioLayoutGroup_orientation, LinearLayout.VERTICAL)
        orientation = index

        attributes.recycle()
        init()
    }

    private fun init() {
        mChildOnCheckedChangeListener = CheckedStateTracker()
        mPassThroughListener = PassThroughHierarchyChangeListener()
        super.setOnHierarchyChangeListener(mPassThroughListener)
    }

    override fun setOnHierarchyChangeListener(listener: ViewGroup.OnHierarchyChangeListener) {
        mPassThroughListener!!.mOnHierarchyChangeListener = listener
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (checkedRadioLayoutId != -1) {
            mProtectFromCheckedChange = true
            setCheckedStateForView(checkedRadioLayoutId, true)
            mProtectFromCheckedChange = false
            setCheckedId(checkedRadioLayoutId)
        }
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child is RadioLayout) {
            if (child.isChecked) {
                mProtectFromCheckedChange = true
                if (checkedRadioLayoutId != -1) {
                    setCheckedStateForView(checkedRadioLayoutId, false)
                }
                mProtectFromCheckedChange = false
                setCheckedId(child.id)
            }
        }
        super.addView(child, index, params)
    }

    private fun check(id: Int) {
        if (id != -1 && id == checkedRadioLayoutId) {
            return
        }
        if (checkedRadioLayoutId != -1) {
            setCheckedStateForView(checkedRadioLayoutId, false)
        }
        if (id != -1) {
            setCheckedStateForView(id, true)
        }
        setCheckedId(id)
    }

    private fun setCheckedId(id: Int) {
        checkedRadioLayoutId = id
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener!!.onCheckedChanged(this, checkedRadioLayoutId)
        }
    }

    private fun setCheckedStateForView(viewId: Int, checked: Boolean) {
        val checkedView = findViewById<View>(viewId)
        if (checkedView != null && checkedView is RadioLayout) {
            checkedView.isChecked = checked
        }
    }

    fun clearCheck() {
        check(-1)
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }

    override fun generateDefaultLayoutParams(): LinearLayout.LayoutParams {
        return LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        event.className = RadioLayoutGroup::class.java.name
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = RadioLayoutGroup::class.java.name
    }

    class LayoutParams : LinearLayout.LayoutParams {
        constructor(c: Context, attrs: AttributeSet) : super(c, attrs)
        constructor(w: Int, h: Int) : super(w, h)

        override fun setBaseAttributes(typedArray: TypedArray,
                                       widthAttr: Int, heightAttr: Int) {

            width = if (typedArray.hasValue(widthAttr)) {
                typedArray.getLayoutDimension(widthAttr, "layout_width")
            } else {
                ViewGroup.LayoutParams.WRAP_CONTENT
            }

            height = if (typedArray.hasValue(heightAttr)) {
                typedArray.getLayoutDimension(heightAttr, "layout_height")
            } else {
                ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(group: RadioLayoutGroup, checkedId: Int)
    }

    private inner class CheckedStateTracker : CompoundLayout.OnCheckedChangeListener {
        override fun onCheckedChanged(compoundLayout: CompoundLayout, checked: Boolean) {
            if (mProtectFromCheckedChange) {
                return
            }

            mProtectFromCheckedChange = true
            if (checkedRadioLayoutId != -1) {
                setCheckedStateForView(checkedRadioLayoutId, false)
            }
            mProtectFromCheckedChange = false

            val id = compoundLayout.id
            setCheckedId(id)
        }
    }

    private inner class PassThroughHierarchyChangeListener : ViewGroup.OnHierarchyChangeListener {
        var mOnHierarchyChangeListener: ViewGroup.OnHierarchyChangeListener? = null

        override fun onChildViewAdded(parent: View, child: View) {
            if (parent === this@RadioLayoutGroup && child is RadioLayout) {
                var id = child.getId()
                if (id == View.NO_ID) {
                    id = View.generateViewId()
                    child.setId(id)
                }
                child.setOnCheckedChangeWidgetListener(
                        mChildOnCheckedChangeListener)
            }
            mOnHierarchyChangeListener?.onChildViewAdded(parent, child)
        }

        override fun onChildViewRemoved(parent: View, child: View) {
            if (parent === this@RadioLayoutGroup && child is RadioLayout) {
                child.setOnCheckedChangeWidgetListener(null)
            }
            mOnHierarchyChangeListener?.onChildViewRemoved(parent, child)
        }
    }
}