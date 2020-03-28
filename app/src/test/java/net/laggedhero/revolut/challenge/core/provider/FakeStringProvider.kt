package net.laggedhero.revolut.challenge.core.provider

class FakeStringProvider(
    private val getString: (id: Int) -> String = { "getString" }
) : StringProvider {
    override fun getString(id: Int): String {
        return getString.invoke(id)
    }
}