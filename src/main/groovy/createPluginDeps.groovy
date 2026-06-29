def deps = project.properties['plugin.deps']
  .split(',')
  .collect { it.trim() }
  .findAll()

project.properties['plugin.deps.lgcy'] = '\n' + deps.collect {
  "  - $it"
}.join('\n')

project.properties['plugin.deps.mdrn'] = '\n' + deps.collect {
  """
  |    ${it}:
  |      load: BEFORE
  |      required: false
  |      join-classpath: true
  """.stripMargin().replaceFirst('^\n', '').stripTrailing()
}.join('\n')
